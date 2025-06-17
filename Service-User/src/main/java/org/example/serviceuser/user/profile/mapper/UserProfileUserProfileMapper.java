/**
 * File Name: UserProfileUserProfileMapper.java
 * Description: 用户个人资料模块的用户详细信息数据访问接口
 * Author: holic512
 * Created Date: 2025-06-12
 * Version: 1.0
 * Usage:
 * 提供对用户详细资料实体的基本CRUD操作，用于用户个人资料页面查询和展示用户的详细信息
 */
package org.example.serviceuser.user.profile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.serviceuser.entity.UserProfile;

@Mapper
public interface UserProfileUserProfileMapper extends BaseMapper<UserProfile> {
}
