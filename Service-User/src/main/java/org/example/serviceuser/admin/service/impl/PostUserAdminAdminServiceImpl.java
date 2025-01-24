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

import org.example.serviceuser.admin.enums.PostUserAdminEnum;
import org.example.serviceuser.admin.mapper.AdminUserMapper;
import org.example.serviceuser.admin.mapper.AdminUserProfileMapper;
import org.example.serviceuser.admin.request.AddUserRequest;
import org.example.serviceuser.admin.service.PostUserAdminService;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostUserAdminAdminServiceImpl implements PostUserAdminService {


    private final AdminUserMapper adminUserMapper;
    private final AdminUserProfileMapper adminUserProfileMapper;

    @Autowired
    public PostUserAdminAdminServiceImpl(AdminUserMapper adminUserMapper, AdminUserProfileMapper adminUserProfileMapper) {
        this.adminUserMapper = adminUserMapper;
        this.adminUserProfileMapper = adminUserProfileMapper;
    }

    @Override
    public PostUserAdminEnum addUser(AddUserRequest addUserRequest) {
        // 判断邮箱 用户名 或 手机号是否重复
        if (adminUserMapper.countUsersByUsername(addUserRequest.getUsername()) > 0) {
            return PostUserAdminEnum.ALREADY_USERNAME;
        }

        if (adminUserMapper.countUsersByEmail(addUserRequest.getEmail()) > 0) {
            return PostUserAdminEnum.ALREADY_EMAIL;
        }

        if (adminUserMapper.countUsersByPhone(addUserRequest.getPhone()) > 0) {
            return PostUserAdminEnum.ALREADY_PHONE;
        }

        // 创建 user对象并插入
        User user = new User();
        user.setUsername(addUserRequest.getUsername());
        user.setEmail(addUserRequest.getEmail());
        user.setPhone(addUserRequest.getPhone());
        user.setPassword(addUserRequest.getPassword());
        user.setStatus(addUserRequest.getStatus());
        adminUserMapper.insert(user);

        // 获取userid 并插入其 userProfile
        Long userId = user.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setAge(addUserRequest.getAge());
        userProfile.setGender(addUserRequest.getGender());
        userProfile.setBio(addUserRequest.getBio());
        userProfile.setNickname(addUserRequest.getNickname());
        userProfile.setAvatar(addUserRequest.getAvatar());

        adminUserProfileMapper.insert(userProfile);

        return PostUserAdminEnum.SUCCESS;
    }
}
