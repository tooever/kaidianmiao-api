package com.kaidianmiao.service;

import com.kaidianmiao.dto.*;
import com.kaidianmiao.entity.Order;

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
}