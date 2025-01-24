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
import org.example.serviceuser.admin.service.PutUserAdminService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.util.SCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ApiResponse updateUser(UserDto userDto) {
        try {
            // 更新用户基本信息
            int userUpdateResult = adminUserMapper.updateUser(userDto); // 更新 user 表

            // 更新用户个人资料信息（直接使用 userDto 中的个人资料字段）
            int userProfileUpdateResult = adminUserProfileMapper.updateUserProfile(userDto); // 更新 user_profile 表

            // 判断是否都成功更新
            if (userUpdateResult > 0 && userProfileUpdateResult > 0) {
                return new ApiResponse(200, "success", null); // 返回成功响应
            } else {
                return new ApiResponse(404, "用户不存在", null); // 如果更新失败（找不到用户或资料）
            }
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return new ApiResponse(500, "Internal Server Error", null);
        }
    }
}
