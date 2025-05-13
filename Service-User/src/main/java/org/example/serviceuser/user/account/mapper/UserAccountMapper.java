/**
 * File Name: UserAccountMapper.java
 * Description: 用户账户数据访问接口
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供用户账户数据的访问方法
 */
package org.example.serviceuser.user.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.serviceuser.entity.User;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserAccountMapper extends BaseMapper<User> {
    /**
     * 检查用户名是否存在（仅查询ID字段）
     * @param username 要检查的用户名
     * @return 存在返回true，否则返回false
     */
    @Select("SELECT id FROM user WHERE username = #{username} LIMIT 1")
    Long selectUsernameExists(@Param("username") String username);

    /**
     * 检查邮箱是否存在（仅查询ID字段）
     * @param email 要检查的邮箱
     * @return 存在返回true，否则返回false
     */
    @Select("SELECT id FROM user WHERE email = #{email} LIMIT 1")
    Long selectEmailExists(@Param("email") String email);
}