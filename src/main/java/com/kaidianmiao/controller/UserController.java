package com.kaidianmiao.controller;

import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.UpdateUserRequest;
import com.kaidianmiao.dto.UserResponse;
import com.kaidianmiao.entity.User;
import com.kaidianmiao.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
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
}