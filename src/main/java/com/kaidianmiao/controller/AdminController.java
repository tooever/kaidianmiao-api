package com.kaidianmiao.controller;

import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.AdminLoginRequest;
import com.kaidianmiao.dto.AdminLoginResponse;
import com.kaidianmiao.entity.Admin;
import com.kaidianmiao.security.JwtTokenProvider;
import com.kaidianmiao.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 管理员登录
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
}