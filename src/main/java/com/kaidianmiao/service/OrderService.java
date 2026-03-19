package com.kaidianmiao.service;

import com.kaidianmiao.dto.*;
import com.kaidianmiao.entity.Order;
import com.kaidianmiao.enums.OrderStatus;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    CreateOrderResponse createOrder(Long userId, CreateOrderRequest request);
    
    /**
     * 获取订单详情
     */
    OrderDetailResponse getOrderDetail(Long orderId);
    
    /**
     * 用户确认支付
     */
    void confirmPaid(Long userId, Long orderId);
    
    /**
     * 管理员审核订单
     */
    void verifyOrder(Long adminId, Long orderId, VerifyOrderRequest request);
    
    /**
     * 获取待审核订单列表
     */
    List<PendingOrderResponse> getPendingOrders();
    
    /**
     * 获取用户订单列表
     */
    List<OrderListItemResponse> getUserOrders(Long userId);
    
    /**
     * 根据ID查找订单
     */
    Order findById(Long orderId);
    
    /**
     * 更新订单
     */
    void updateOrder(Order order);
    
    /**
     * 获取管理后台数据统计
     */
    DashboardResponse getDashboardStats();
    
    /**
     * 获取管理后台订单列表（分页）
     * @param page 页码
     * @param size 每页大小
     * @param status 订单状态筛选（可选）
     * @return 分页订单列表
     */
    PageResponse<AdminOrderListItem> getAdminOrders(Integer page, Integer size, OrderStatus status);
}