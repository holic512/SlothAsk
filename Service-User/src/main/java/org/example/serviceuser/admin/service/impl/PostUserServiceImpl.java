/**
 * File Name: PostUserServiceImpl.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.service.impl;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.mapper.AdminUserMapper;
import org.example.serviceuser.admin.mapper.AdminUserProfileMapper;
import org.example.serviceuser.admin.service.PostUserService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PostUserServiceImpl implements PostUserService {

    private final AdminUserMapper adminUserMapper;
    private final AdminUserProfileMapper adminUserProfileMapper;

    @Autowired
    public PostUserServiceImpl(AdminUserMapper adminUserMapper, AdminUserProfileMapper adminUserProfileMapper) {
        this.adminUserMapper = adminUserMapper;
        this.adminUserProfileMapper = adminUserProfileMapper;
    }

    @Override
    public ApiResponse addUser(UserDto userDto) {
        // 如果没有传递密码，生成随机密码
        if (userDto.getPassword() == null) {
            userDto.setPassword(generateRandomPassword());
        }

        // 创建 User 实体对象
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword());
        user.setStatus(userDto.getStatus());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 插入用户到数据库
        int userResult = adminUserMapper.insert(user);

        if (userResult > 0 && user.getId() != null) {
            // 创建 UserProfile 实体对象
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());  // 使用插入后的 userId
            userProfile.setNickname(userDto.getNickname());
            userProfile.setGender(userDto.getGender());
            userProfile.setAge(userDto.getAge());
            userProfile.setAvatar(userDto.getAvatar());
            userProfile.setBio(userDto.getBio());
            userProfile.setCreateTime(LocalDateTime.now());
            userProfile.setUpdateTime(LocalDateTime.now());

            // 插入用户资料到数据库
            adminUserProfileMapper.insert(userProfile);

            return new ApiResponse(200, "success", null);
        } else {
            return new ApiResponse(500, "failed", null);
        }
    }

    // 随机生成密码
    private String generateRandomPassword() {
        // 可以使用密码生成工具或者随机数生成函数
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);  // 生成一个8位的随机密码
    }
}
