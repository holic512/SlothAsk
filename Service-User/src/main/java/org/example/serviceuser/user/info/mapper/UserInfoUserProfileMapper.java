/**
 * File Name: UserInfoUserProfileMapper.java
 * Description: 用户资料数据访问接口
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供用户资料数据库访问方法
 */
package org.example.serviceuser.user.info.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.serviceuser.entity.UserProfile;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserInfoUserProfileMapper extends BaseMapper<UserProfile> {
    
    /**
     * 根据用户ID查询用户资料
     * 
     * @param userId 用户ID
     * @return 用户资料对象，如果不存在则返回null
     */
    @Select("SELECT * FROM user_profile WHERE user_id = #{userId}")
    UserProfile selectByUserId(Long userId);
} 