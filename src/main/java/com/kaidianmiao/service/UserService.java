package com.kaidianmiao.service;

import com.kaidianmiao.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据 openid 查找用户
     */
    User findByOpenid(String openid);
    
    /**
     * 根据 ID 查找用户
     */
    User findById(Long id);
    
    /**
     * 根据手机号查找用户
     */
    User findByPhone(String phone);
    
    /**
     * 创建新用户
     */
    User createUser(String openid);
    
    /**
     * 保存用户（新建或更新）
     */
    User save(User user);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long userId, String nickname, String avatarUrl);
}