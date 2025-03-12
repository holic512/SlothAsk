/**
 * File Name: UserSignMapper.java
 * Description: 用户登录注册数据访问接口，提供用户信息查询功能
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * 用于访问用户表数据，提供根据邮箱查询用户信息的功能
 */
package org.example.serviceuser.user.sign.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.serviceuser.entity.User;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户登录注册数据访问接口
 * 继承MyBatis-Plus的BaseMapper，提供用户表的基本CRUD操作和自定义查询方法
 */
@Mapper
public interface UserSignUserMapper extends BaseMapper<User> {

    /**
     * 根据邮箱查询用户信息
     * 用于验证用户是否已注册
     * 
     * @param email 用户邮箱
     * @return User 用户信息，如果不存在则返回null
     */
    @Select("select * from user where email = #{email} ")
    User getUserByEmail(@Param("email") String email);

    /**
     * 根据用户名查询用户信息
     * 用于验证用户名是否已被注册
     * 
     * @param username 用户名
     * @return User 用户信息，如果不存在则返回null
     */
    @Select("select * from user where username = #{username} ")
    User getUserByUsername(@Param("username") String username);
}
