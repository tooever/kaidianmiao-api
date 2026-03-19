package com.kaidianmiao.controller;

import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.*;
import com.kaidianmiao.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * 创建订单
     * POST /api/order
     */
    @PostMapping
    public Result<CreateOrderResponse> createOrder(
            HttpServletRequest request,
            @Valid @RequestBody CreateOrderRequest createRequest) {
        
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED, "未授权", "请先登录");
        }
        
        CreateOrderResponse response = orderService.createOrder(userId, createRequest);
        return Result.success(response);
    }
    
    /**
     * 获取订单详情
     * GET /api/order/{id}
     */
    @GetMapping("/{id}")
    public Result<OrderDetailResponse> getOrderDetail(@PathVariable("id") Long orderId) {
        OrderDetailResponse response = orderService.getOrderDetail(orderId);
        
        if (response == null) {
            return Result.error(ErrorCode.ORDER_NOT_FOUND, "订单不存在", "订单不存在");
        }
        
        return Result.success(response);
    }
    
    /**
     * 用户确认支付
     * POST /api/order/{id}/confirm-paid
     */
    @PostMapping("/{id}/confirm-paid")
    public Result<Void> confirmPaid(
            HttpServletRequest request,
            @PathVariable("id") Long orderId) {
        
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED, "未授权", "请先登录");
        }
        
        orderService.confirmPaid(userId, orderId);
        return Result.success();
    }
}