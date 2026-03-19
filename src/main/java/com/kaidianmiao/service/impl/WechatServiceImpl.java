package com.kaidianmiao.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaidianmiao.common.BusinessException;
import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.dto.WechatLoginResponse;
import com.kaidianmiao.entity.User;
import com.kaidianmiao.security.JwtTokenProvider;
import com.kaidianmiao.service.UserService;
import com.kaidianmiao.service.WechatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 微信服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatServiceImpl implements WechatService {
    
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${wechat.appid}")
    private String appId;
    
    @Value("${wechat.secret}")
    private String appSecret;
    
    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    
    @Override
    public WechatLoginResponse login(String code) {
        // 调用微信 code2Session 接口
        String url = String.format(CODE2SESSION_URL, appId, appSecret, code);
        log.info("Calling WeChat code2Session API");
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String body = response.getBody();
            log.debug("WeChat response: {}", body);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(body, Map.class);
            
            // 检查是否有错误
            if (result.containsKey("errcode") && (Integer) result.get("errcode") != 0) {
                Integer errcode = (Integer) result.get("errcode");
                String errmsg = (String) result.get("errmsg");
                log.error("WeChat API error: {} - {}", errcode, errmsg);
                throw new BusinessException(ErrorCode.LOGIN_FAILED, "微信登录失败: " + errmsg);
            }
            
            String openid = (String) result.get("openid");
            if (openid == null || openid.isEmpty()) {
                log.error("WeChat response missing openid: {}", body);
                throw new BusinessException(ErrorCode.LOGIN_FAILED, "获取微信用户标识失败");
            }
            
            // 查找或创建用户
            User user = userService.findByOpenid(openid);
            if (user == null) {
                user = userService.createUser(openid);
            }
            
            // 生成 JWT Token
            String token = jwtTokenProvider.generateUserToken(user.getId());
            
            // 构建响应
            return WechatLoginResponse.builder()
                    .token(token)
                    .user(WechatLoginResponse.UserInfo.builder()
                            .id(user.getId())
                            .nickname(user.getNickname())
                            .avatarUrl(user.getAvatarUrl())
                            .build())
                    .build();
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("WeChat login failed", e);
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "微信登录失败: " + e.getMessage());
        }
    }
}