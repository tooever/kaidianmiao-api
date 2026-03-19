package com.kaidianmiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaidianmiao.common.BusinessException;
import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.dto.*;
import com.kaidianmiao.entity.AnalysisTask;
import com.kaidianmiao.entity.Order;
import com.kaidianmiao.entity.PaymentLog;
import com.kaidianmiao.entity.User;
import com.kaidianmiao.enums.OrderStatus;
import com.kaidianmiao.enums.TaskStatus;
import com.kaidianmiao.mapper.OrderMapper;
import com.kaidianmiao.mapper.PaymentLogMapper;
import com.kaidianmiao.service.AnalysisTaskService;
import com.kaidianmiao.service.MockAnalysisService;
import com.kaidianmiao.service.OrderService;
import com.kaidianmiao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderMapper orderMapper;
    private final PaymentLogMapper paymentLogMapper;
    private final AnalysisTaskService analysisTaskService;
    private final MockAnalysisService mockAnalysisService;
    private final UserService userService;
    
    /**
     * 产品价格映射
     */
    private static final BigDecimal PRICE_BASIC = new BigDecimal("9.9");
    private static final BigDecimal PRICE_FULL = new BigDecimal("29.9");
    
    /**
     * 产品名称映射
     */
    private static final String PRODUCT_NAME_BASIC = "基础版";
    private static final String PRODUCT_NAME_FULL = "完整版";
    
    @Override
    @Transactional
    public CreateOrderResponse createOrder(Long userId, CreateOrderRequest request) {
        // 验证任务存在且属于当前用户
        AnalysisTask task = analysisTaskService.findById(request.getTaskId());
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND, "任务不存在");
        }
        if (!task.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作此任务");
        }
        
        // 验证产品类型
        if (request.getProductType() != 1 && request.getProductType() != 2) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER, "产品类型无效");
        }
        
        // 检查任务是否已有订单
        Order existingOrder = orderMapper.selectOne(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getTaskId, request.getTaskId())
        );
        if (existingOrder != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_ENTRY, "该任务已创建订单");
        }
        
        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTaskId(request.getTaskId());
        order.setProductType(request.getProductType());
        order.setAmount(request.getProductType() == 1 ? PRICE_BASIC : PRICE_FULL);
        order.setStatus(OrderStatus.UNPAID);
        order.setOrderNo(generateOrderNo());
        
        orderMapper.insert(order);
        
        log.info("创建订单成功, orderId={}, orderNo={}, userId={}", order.getId(), order.getOrderNo(), userId);
        
        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .amount(order.getAmount())
                .status(order.getStatus().getValue())
                .build();
    }
    
    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return null;
        }
        
        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .amount(order.getAmount())
                .productType(order.getProductType())
                .productName(getProductName(order.getProductType()))
                .status(order.getStatus().getValue())
                .statusLabel(order.getStatus().getLabel())
                .taskId(order.getTaskId())
                .createdAt(order.getCreatedAt())
                .userConfirmTime(order.getUserConfirmTime())
                .adminVerifyTime(order.getAdminVerifyTime())
                .build();
    }
    
    @Override
    @Transactional
    public void confirmPaid(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "订单不存在");
        }
        
        // 验证订单属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作此订单");
        }
        
        // 状态转换校验
        validateStatusTransition(order.getStatus(), OrderStatus.PENDING_VERIFY);
        
        // 防重复提交：已经是 pending_verify 状态直接返回成功
        if (order.getStatus() == OrderStatus.PENDING_VERIFY) {
            log.info("订单已是待审核状态, orderId={}", orderId);
            return;
        }
        
        // 更新状态
        order.setStatus(OrderStatus.PENDING_VERIFY);
        order.setUserConfirmTime(LocalDateTime.now());
        
        orderMapper.updateById(order);
        
        log.info("用户确认支付成功, orderId={}, userId={}", orderId, userId);
    }
    
    @Override
    @Transactional
    public void verifyOrder(Long adminId, Long orderId, VerifyOrderRequest request) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "订单不存在");
        }
        
        // 状态转换校验
        if (order.getStatus() != OrderStatus.PENDING_VERIFY) {
            throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION, 
                "订单状态不允许审核，当前状态：" + order.getStatus().getLabel());
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (request.getApprove()) {
            // 审核通过：pending_verify → paid
            order.setStatus(OrderStatus.PAID);
            order.setAdminVerifyTime(now);
            order.setAdminId(adminId);
            orderMapper.updateById(order);
            
            // 写入支付流水
            PaymentLog paymentLog = new PaymentLog();
            paymentLog.setOrderId(orderId);
            paymentLog.setUserId(order.getUserId());
            paymentLog.setAmount(order.getAmount());
            paymentLog.setPaymentMethod("manual_verify");
            paymentLog.setPaidAt(now);
            paymentLog.setRemark(request.getRemark());
            paymentLogMapper.insert(paymentLog);
            
            log.info("管理员审核通过, orderId={}, adminId={}", orderId, adminId);
            
            // 触发 Mock 分析
            AnalysisTask task = analysisTaskService.findById(order.getTaskId());
            if (task != null) {
                mockAnalysisService.executeMockAnalysis(task.getId());
                log.info("触发分析任务, taskId={}", task.getId());
            }
        } else {
            // 审核拒绝：pending_verify → refunded
            order.setStatus(OrderStatus.REFUNDED);
            order.setAdminVerifyTime(now);
            order.setAdminId(adminId);
            orderMapper.updateById(order);
            
            log.info("管理员审核拒绝, orderId={}, adminId={}", orderId, adminId);
        }
    }
    
    @Override
    public List<PendingOrderResponse> getPendingOrders() {
        List<Order> orders = orderMapper.selectList(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, OrderStatus.PENDING_VERIFY)
                .orderByDesc(Order::getUserConfirmTime)
        );
        
        return orders.stream().map(order -> {
            // 获取用户信息
            User user = userService.findById(order.getUserId());
            String nickname = user != null ? user.getNickname() : "未知用户";
            
            return PendingOrderResponse.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .amount(order.getAmount())
                    .productType(order.getProductType())
                    .productName(getProductName(order.getProductType()))
                    .status(order.getStatus().getValue())
                    .userId(order.getUserId())
                    .userNickname(nickname)
                    .taskId(order.getTaskId())
                    .createdAt(order.getCreatedAt())
                    .userConfirmTime(order.getUserConfirmTime())
                    .build();
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<OrderListItemResponse> getUserOrders(Long userId) {
        List<Order> orders = orderMapper.selectList(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt)
        );
        
        return orders.stream().map(order -> OrderListItemResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .amount(order.getAmount())
                .productType(order.getProductType())
                .productName(getProductName(order.getProductType()))
                .status(order.getStatus().getValue())
                .statusLabel(order.getStatus().getLabel())
                .createdAt(order.getCreatedAt())
                .build()).collect(Collectors.toList());
    }
    
    @Override
    public Order findById(Long orderId) {
        return orderMapper.selectById(orderId);
    }
    
    @Override
    public void updateOrder(Order order) {
        orderMapper.updateById(order);
    }
    
    /**
     * 生成订单号
     * 格式：KDM + 时间戳 + 4位随机数
     * 例如：KDM20260319143000001234
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "KDM" + timestamp + random;
    }
    
    /**
     * 状态转换校验
     */
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus targetStatus) {
        // unpaid → pending_verify：允许
        if (currentStatus == OrderStatus.UNPAID && targetStatus == OrderStatus.PENDING_VERIFY) {
            return;
        }
        // pending_verify → paid：允许（管理员审核）
        if (currentStatus == OrderStatus.PENDING_VERIFY && targetStatus == OrderStatus.PAID) {
            return;
        }
        // pending_verify → refunded：允许（管理员拒绝）
        if (currentStatus == OrderStatus.PENDING_VERIFY && targetStatus == OrderStatus.REFUNDED) {
            return;
        }
        
        // 非法状态转换
        throw new BusinessException(ErrorCode.INVALID_STATE_TRANSITION,
            "订单状态转换非法，当前状态：" + currentStatus.getLabel() + "，目标状态：" + targetStatus.getLabel());
    }
    
    /**
     * 获取产品名称
     */
    private String getProductName(Integer productType) {
        return productType == 1 ? PRODUCT_NAME_BASIC : PRODUCT_NAME_FULL;
    }
}