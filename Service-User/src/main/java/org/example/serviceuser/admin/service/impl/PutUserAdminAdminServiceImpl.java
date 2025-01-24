/**
 * File Name: PutUserServiceImpl.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.service.impl;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.enums.PutUserAdminEnum;
import org.example.serviceuser.admin.mapper.AdminUserMapper;
import org.example.serviceuser.admin.mapper.AdminUserProfileMapper;
import org.example.serviceuser.admin.request.UpdateUserRequest;
import org.example.serviceuser.admin.service.PutUserAdminService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserProfile;
import org.example.serviceuser.util.SCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PutUserAdminAdminServiceImpl implements PutUserAdminService {

    private final AdminUserMapper adminUserMapper;
    private final AdminUserProfileMapper adminUserProfileMapper;

    @Autowired
    public PutUserAdminAdminServiceImpl(AdminUserMapper adminUserMapper, AdminUserProfileMapper adminUserProfileMapper) {
        this.adminUserMapper = adminUserMapper;
        this.adminUserProfileMapper = adminUserProfileMapper;
    }


    @Override
    public PutUserAdminEnum UpdatePassword(Long id, String password) {

        // 调用密码加密
        String hashPassword = SCryptUtil.hashPassword(password);


        // 调用Mapper方法，返回受影响的行数大于0，表示更新成功
        int result = adminUserMapper.updatePasswordByUserId(id, hashPassword);

        if (result > 0) {
            return PutUserAdminEnum.SUCCESS;  // 更新成功
        } else {
            return PutUserAdminEnum.FAILURE;  // 更新失败
        }
    }

    @Override
    @Transactional
    public PutUserAdminEnum updateUser(UpdateUserRequest userRequest) {
        // 获取新数据是否于数据库数据冲突
        Long existingIdByPhone = adminUserMapper.SelectIdByPhone(userRequest.getPhone());
        if (existingIdByPhone != null && !existingIdByPhone.equals(userRequest.getId())) {
            return PutUserAdminEnum.ALREADY_PHONE;
        }

        Long existingIdByUsername = adminUserMapper.SelectIdByUsername(userRequest.getUsername());
        if (existingIdByUsername != null && !existingIdByUsername.equals(userRequest.getId())) {
            return PutUserAdminEnum.ALREADY_USERNAME;
        }

        Long existingIdByEmail = adminUserMapper.SelectIdByEmail(userRequest.getEmail());
        if (existingIdByEmail != null && !existingIdByEmail.equals(userRequest.getId())) {
            return PutUserAdminEnum.ALREADY_EMAIL;
        }


        // 更新用户基本信息
        User user = new User();
        user.setId(userRequest.getId());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setStatus(userRequest.getStatus());
        int userUpdateResult = adminUserMapper.updateById(user);

        // 更新用户个人资料信息
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userRequest.getId());
        userProfile.setAge(userRequest.getAge());
        userProfile.setBio(userRequest.getBio());
        userProfile.setNickname(userRequest.getNickname());
        userProfile.setGender(userRequest.getGender());
        userProfile.setAvatar(userRequest.getAvatar());
        int userProfileUpdateResult = adminUserProfileMapper.updateByUserId(userProfile); // 更新 user_profile 表

        // 判断是否都成功更新
        if (userUpdateResult > 0 && userProfileUpdateResult > 0) {
            return PutUserAdminEnum.SUCCESS;
        } else {
            return PutUserAdminEnum.FAILURE;
        }
    }
}
