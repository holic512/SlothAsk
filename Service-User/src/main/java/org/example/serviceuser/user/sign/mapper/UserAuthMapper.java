/**
 * File Name: UserAuthMapper.java
 * Description: 第三方认证数据访问接口
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * 提供第三方认证数据的访问方法
 */
package org.example.serviceuser.user.sign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.serviceuser.entity.UserAuth;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
    
    /**
     * 根据提供者和openId查询用户授权信息
     * 
     * @param provider 提供者 (例如: github, wechat)
     * @param openId 第三方平台用户ID
     * @return 用户授权信息，如果不存在则返回null
     */
    @Select("SELECT * FROM user_auth WHERE provider = #{provider} AND open_id = #{openId} AND is_deleted = 0")
    UserAuth findByProviderAndOpenId(@Param("provider") String provider, @Param("openId") String openId);
    
    /**
     * 根据用户ID和提供者查询用户授权信息
     * 
     * @param userId 用户ID
     * @param provider 提供者 (例如: github, wechat)
     * @return 用户授权信息，如果不存在则返回null
     */
    @Select("SELECT * FROM user_auth WHERE user_id = #{userId} AND provider = #{provider} AND is_deleted = 0")
    UserAuth findByUserIdAndProvider(@Param("userId") Long userId, @Param("provider") String provider);
} 