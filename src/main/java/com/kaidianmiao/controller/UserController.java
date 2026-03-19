package com.kaidianmiao.controller;

import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.OrderListItemResponse;
import com.kaidianmiao.dto.UpdateUserRequest;
import com.kaidianmiao.dto.UserReportListItem;
import com.kaidianmiao.dto.UserResponse;
import com.kaidianmiao.entity.User;
import com.kaidianmiao.service.AnalysisTaskService;
import com.kaidianmiao.service.OrderService;
import com.kaidianmiao.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final AnalysisTaskService analysisTaskService;
    private final OrderService orderService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<UserResponse> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(40001, "未授权", "请先登录");
        }
        
        User user = userService.findById(userId);
        if (user == null) {
            return Result.error(40401, "用户不存在", "用户不存在");
        }
        
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .build();
        
        return Result.success(response);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/me")
    public Result<UserResponse> updateUser(HttpServletRequest request, @Valid @RequestBody UpdateUserRequest updateRequest) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(40001, "未授权", "请先登录");
        }
        
        User user = userService.updateUser(userId, updateRequest.getNickname(), updateRequest.getAvatarUrl());
        if (user == null) {
            return Result.error(40401, "用户不存在", "用户不存在");
        }
        
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .build();
        
        return Result.success(response);
    }
    
    /**
     * 获取用户报告列表
     * GET /api/user/reports
     */
    @GetMapping("/reports")
    public Result<List<UserReportListItem>> getUserReports(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED, "未授权", "请先登录");
        }
        
        List<UserReportListItem> reports = analysisTaskService.getUserReports(userId);
        return Result.success(reports);
    }
    
    /**
     * 获取用户订单列表
     * GET /api/user/orders
     */
    @GetMapping("/orders")
    public Result<List<OrderListItemResponse>> getUserOrders(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED, "未授权", "请先登录");
        }
        
        List<OrderListItemResponse> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }
}