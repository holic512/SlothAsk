/**
 * File Name: PostAdminController.java
 * Description: 用户管理系统-管理员新增用户控制器
 * Author: holic512
 * Created Date: 2025-01-23
 * Version: 1.0
 * Usage: 提供管理员添加新用户的接口
 */
package org.example.serviceuser.admin.controller;

import jakarta.validation.Valid;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.admin.enums.PostUserAdminEnum;
import org.example.serviceuser.admin.request.AddUserRequest;
import org.example.serviceuser.admin.service.PostUserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class PostUserAdminController {

    private final PostUserAdminService postUserAdminService;

    @Autowired
    public PostUserAdminController(PostUserAdminService postUserAdminService) {
        this.postUserAdminService = postUserAdminService;
    }

    /**
     * 添加新用户
     * @param userRequest 新用户的信息请求对象
     * @return ApiResponse 添加操作的响应结果
     * 成功返回: 200 状态码和成功消息
     * 失败返回: 403 状态码和失败原因
     */
    @PostMapping("/addUser")
    public ApiResponse addUser(@Valid @RequestBody AddUserRequest userRequest) {

        PostUserAdminEnum result = postUserAdminService.addUser(userRequest);

        if (result == PostUserAdminEnum.SUCCESS) {
            return new ApiResponse(200, "插入成功");
        } else {
            return new ApiResponse(403, result.getMessage());
        }

    }
}
