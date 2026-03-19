package com.kaidianmiao.controller;

import com.kaidianmiao.common.Result;
import com.kaidianmiao.dto.WechatLoginRequest;
import com.kaidianmiao.dto.WechatLoginResponse;
import com.kaidianmiao.service.WechatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final WechatService wechatService;
    
    /**
     * 微信登录
     */
    @PostMapping("/wechat-login")
    public Result<WechatLoginResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest request) {
        log.info("WeChat login request received");
        WechatLoginResponse response = wechatService.login(request.getCode());
        return Result.success(response);
    }
}