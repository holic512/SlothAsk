/**
 * File Name: PostAuthController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.auth.controller;

import org.example.serviceadmin.auth.dto.LoginRequest;
import org.example.serviceadmin.auth.enmus.LoginEnum;
import org.example.serviceadmin.auth.service.PostAuthService;
import org.example.serviceadmin.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PostAuthController {
    private final PostAuthService postAuthService;

    @Autowired
    public PostAuthController(PostAuthService postAuthService) {
        this.postAuthService = postAuthService;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return new ApiResponse(404, "用户名和密码不能为空");
        }

        // 调用服务类
        LoginEnum result = postAuthService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (result == LoginEnum.Success) {
            return new ApiResponse(200, result.getMessage());
        } else {
            return new ApiResponse(400, result.getMessage());
        }

    }
}
