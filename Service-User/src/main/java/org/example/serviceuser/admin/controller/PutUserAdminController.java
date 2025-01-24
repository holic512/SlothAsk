/**
 * File Name: PutAdminController.java
 * Description: 用户管理系统-管理员更新用户控制器
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 提供管理员更新用户信息的相关接口，包括更新密码和更新用户基本信息功能
 */
package org.example.serviceuser.admin.controller;

import jakarta.validation.Valid;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.enums.PutUserAdminEnum;
import org.example.serviceuser.admin.request.UpdatePasswordRequest;
import org.example.serviceuser.admin.request.UpdateUserRequest;
import org.example.serviceuser.admin.service.PutUserAdminService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class PutUserAdminController {

    private final PutUserAdminService putUserAdminService;

    @Autowired
    public PutUserAdminController(PutUserAdminService putUserAdminService) {
        this.putUserAdminService = putUserAdminService;
    }

    /**
     * 更新用户密码
     * @param updatePasswordRequest 包含用户ID和新密码的请求对象
     * @return ApiResponse 更新密码操作的响应结果
     * 成功返回: 200 状态码和成功消息
     * 失败返回: 400 状态码和失败消息
     */
    @PutMapping("/UpdatePassword")
    public ApiResponse UpdatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        PutUserAdminEnum result = putUserAdminService.UpdatePassword(updatePasswordRequest.getId(), updatePasswordRequest.getNewPassword());
        if (result == PutUserAdminEnum.SUCCESS) {
            return new ApiResponse(200, "更新密码成功");
        } else {
            return new ApiResponse(400, "更新密码失败");
        }
    }

    /**
     * 更新用户基本信息
     * @param userRequest 包含用户更新信息的请求对象
     * @return ApiResponse 更新用户信息的响应结果
     * 成功返回: 200 状态码和成功消息
     * 失败返回: 400 状态码和失败原因
     */
    @PutMapping("/updateUser")
    public ApiResponse updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        PutUserAdminEnum result = putUserAdminService.updateUser(userRequest);
        if (result == PutUserAdminEnum.SUCCESS) {
            return new ApiResponse(200, "更新用户信息成功");
        } else if (result == PutUserAdminEnum.FAILURE) {
            return new ApiResponse(400, "更新用户信息失败");
        } else {
            return new ApiResponse(400, result.getMessage());
        }
    }
}
