package com.kaidianmiao.service;

import com.kaidianmiao.entity.Admin;

/**
 * 管理员服务接口
 */
public interface AdminService {
    
    /**
     * 根据用户名查找管理员
     */
    Admin findByUsername(String username);
    
    /**
     * 验证密码
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);
}