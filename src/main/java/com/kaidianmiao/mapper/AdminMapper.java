package com.kaidianmiao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaidianmiao.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 管理员 Mapper
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    
    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin findByUsername(String username);
}