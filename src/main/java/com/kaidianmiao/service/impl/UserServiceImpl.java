package com.kaidianmiao.service.impl;

import com.kaidianmiao.entity.User;
import com.kaidianmiao.mapper.UserMapper;
import com.kaidianmiao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    
    @Override
    public User findByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }
    
    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
    
    @Override
    public User createUser(String openid) {
        User user = new User();
        user.setOpenid(openid);
        user.setNickname("开店喵用户");
        userMapper.insert(user);
        log.info("Created new user with openid: {}", openid);
        return user;
    }
    
    @Override
    public User updateUser(Long userId, String nickname, String avatarUrl) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }
        userMapper.updateById(user);
        return user;
    }
}