/**
 * File Name: AuthAdminUserMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.serviceadmin.entiy.AdminUser;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.Mapping;

@Mapper
public interface AuthAdminUserMapper extends BaseMapper<AdminUser> {

    @Select("SELECT Password FROM admin_user WHERE username = #{username}")
    String selectPasswordByUserName(@Param("username") String username);
}
