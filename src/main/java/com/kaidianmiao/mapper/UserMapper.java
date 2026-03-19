package com.kaidianmiao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaidianmiao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User findByOpenid(String openid);
}