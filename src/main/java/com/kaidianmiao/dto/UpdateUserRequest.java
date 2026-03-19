package com.kaidianmiao.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户信息请求
 */
@Data
public class UpdateUserRequest {
    
    @Size(max = 64, message = "昵称长度不能超过64个字符")
    private String nickname;
    
    @Size(max = 256, message = "头像URL长度不能超过256个字符")
    private String avatarUrl;
}