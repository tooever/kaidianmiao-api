package com.kaidianmiao.controller;

import com.kaidianmiao.common.Result;
import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.dto.WechatLoginRequest;
import com.kaidianmiao.dto.WechatLoginResponse;
import com.kaidianmiao.entity.User;
import com.kaidianmiao.security.JwtTokenProvider;
import com.kaidianmiao.service.UserService;
import com.kaidianmiao.service.WechatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final WechatService wechatService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * 微信登录
     */
    @PostMapping("/wechat-login")
    public Result<WechatLoginResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest request) {
        log.info("WeChat login request received");
        WechatLoginResponse response = wechatService.login(request.getCode());
        return Result.success(response);
    }
    
    /**
     * 测试登录（仅开发环境可用）
     * 用于联调和测试，通过手机号快速获取登录 token
     */
    @PostMapping("/test-login")
    public Result<WechatLoginResponse> testLogin(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        if (phone == null || phone.isEmpty()) {
            return Result.error(ErrorCode.INVALID_PARAMETER, "手机号不能为空", "请输入手机号");
        }
        
        // 查找或创建测试用户
        User user = userService.findByPhone(phone);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setOpenid("test_" + phone);
            user.setNickname("测试用户_" + phone.substring(phone.length() - 4));
            userService.save(user);
        }
        
        String token = jwtTokenProvider.generateUserToken(user.getId());
        
        WechatLoginResponse response = WechatLoginResponse.builder()
                .token(token)
                .user(WechatLoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .avatarUrl(user.getAvatarUrl())
                        .build())
                .build();
        
        log.info("Test login success for phone: {}, userId: {}", phone, user.getId());
        return Result.success(response);
    }
}