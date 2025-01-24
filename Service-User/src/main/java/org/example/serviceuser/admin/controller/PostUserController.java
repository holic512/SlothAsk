/**
 * File Name: PostAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-23
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.controller;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.service.PostUserService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class PostUserController {

    private final PostUserService postUserService;

    @Autowired
    public PostUserController(PostUserService postUserService) {
        this.postUserService = postUserService;
    }

    @PostMapping("/add")
    public ApiResponse addUser(@RequestBody UserDto userDto) {
        // 校验请求数据
        if (userDto == null || userDto.getUsername() == null || userDto.getEmail() == null) {
            return new ApiResponse(400, "Username and email are required", null);
        }

        // 返回服务层的结果
        return postUserService.addUser(userDto);
    }
}
