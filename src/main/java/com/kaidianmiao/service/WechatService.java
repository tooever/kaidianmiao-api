package com.kaidianmiao.service;

import com.kaidianmiao.dto.WechatLoginResponse;

/**
 * 微信服务接口
 */
public interface WechatService {
    
    /**
     * 微信登录
     * @param code 微信授权码
     * @return 登录响应（包含 token 和用户信息）
     */
    WechatLoginResponse login(String code);
}