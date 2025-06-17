/**
 * File Name: UserProfileUserMapper.java
 * Description: 用户个人资料模块的用户数据访问接口
 * Author: holic512
 * Created Date: 2025-06-12
 * Version: 1.0
 * Usage:
 * 提供对用户实体的基本CRUD操作，用于用户个人资料页面查询用户基本信息
 */
package org.example.serviceuser.user.profile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.serviceuser.entity.User;

@Mapper
public interface UserProfileUserMapper extends BaseMapper<User> {
}
