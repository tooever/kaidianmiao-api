package com.kaidianmiao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求
 */
@Data
public class WechatLoginRequest {
    
    @NotBlank(message = "授权码不能为空")
    private String code;
}