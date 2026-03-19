package com.kaidianmiao.controller;

import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.*;
import com.kaidianmiao.entity.Admin;
import com.kaidianmiao.enums.OrderStatus;
import com.kaidianmiao.security.JwtTokenProvider;
import com.kaidianmiao.service.AdminService;
import com.kaidianmiao.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderService orderService;
    
    /**
     * 管理员登录
     * POST /api/admin/login
     */
    @PostMapping("/login")
    public Result<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        log.info("Admin login attempt: {}", request.getUsername());
        
        // 查找管理员
        Admin admin = adminService.findByUsername(request.getUsername());
        if (admin == null) {
            log.warn("Admin not found: {}", request.getUsername());
            return Result.error(ErrorCode.LOGIN_FAILED, "用户名或密码错误", "用户名或密码错误");
        }
        
        // 验证密码
        if (!adminService.verifyPassword(request.getPassword(), admin.getPasswordHash())) {
            log.warn("Invalid password for admin: {}", request.getUsername());
            return Result.error(ErrorCode.LOGIN_FAILED, "用户名或密码错误", "用户名或密码错误");
        }
        
        // 生成 Token
        String token = jwtTokenProvider.generateAdminToken(admin.getId());
        
        // 构建响应
        AdminLoginResponse response = AdminLoginResponse.builder()
                .token(token)
                .admin(AdminLoginResponse.AdminInfo.builder()
                        .id(admin.getId())
                        .username(admin.getUsername())
                        .build())
                .build();
        
        log.info("Admin login successful: {}", request.getUsername());
        return Result.success(response);
    }
    
    /**
     * 获取管理后台数据统计
     * GET /api/admin/dashboard
     */
    @GetMapping("/dashboard")
    public Result<DashboardResponse> getDashboard(HttpServletRequest request) {
        // 验证管理员权限
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error(ErrorCode.ADMIN_REQUIRED, "需要管理员权限", "需要管理员权限");
        }
        
        DashboardResponse dashboard = orderService.getDashboardStats();
        return Result.success(dashboard);
    }
    
    /**
     * 获取全部订单列表（分页）
     * GET /api/admin/orders?page=1&size=20&status=pending_verify
     */
    @GetMapping("/orders")
    public Result<PageResponse<AdminOrderListItem>> getOrders(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String status) {
        
        // 验证管理员权限
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error(ErrorCode.ADMIN_REQUIRED, "需要管理员权限", "需要管理员权限");
        }
        
        // 解析状态参数
        OrderStatus orderStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = OrderStatus.fromValue(status);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid order status: {}", status);
                return Result.error(ErrorCode.INVALID_PARAMETER, "无效的订单状态", "无效的订单状态");
            }
        }
        
        PageResponse<AdminOrderListItem> result = orderService.getAdminOrders(page, size, orderStatus);
        return Result.success(result);
    }
    
    /**
     * 获取待审核订单列表
     * GET /api/admin/orders/pending
     */
    @GetMapping("/orders/pending")
    public Result<List<PendingOrderResponse>> getPendingOrders(HttpServletRequest request) {
        // 验证管理员权限
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error(ErrorCode.ADMIN_REQUIRED, "需要管理员权限", "需要管理员权限");
        }
        
        List<PendingOrderResponse> orders = orderService.getPendingOrders();
        return Result.success(orders);
    }
    
    /**
     * 审核订单
     * POST /api/admin/order/{id}/verify
     */
    @PostMapping("/order/{id}/verify")
    public Result<Void> verifyOrder(
            HttpServletRequest request,
            @PathVariable("id") Long orderId,
            @Valid @RequestBody VerifyOrderRequest verifyRequest) {
        
        // 验证管理员权限并获取 adminId
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error(ErrorCode.ADMIN_REQUIRED, "需要管理员权限", "需要管理员权限");
        }
        
        // JWT 中 subject 就是 id，存储在 userId 属性中
        Long adminId = (Long) request.getAttribute("userId");
        if (adminId == null) {
            return Result.error(ErrorCode.UNAUTHORIZED, "未授权", "请先登录");
        }
        
        orderService.verifyOrder(adminId, orderId, verifyRequest);
        return Result.success();
    }
}