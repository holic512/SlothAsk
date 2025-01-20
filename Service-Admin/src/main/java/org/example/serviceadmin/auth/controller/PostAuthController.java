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

import org.apache.commons.lang3.tuple.Pair;
import org.example.serviceadmin.auth.dto.LoginRequest;
import org.example.serviceadmin.auth.enmus.LoginEnum;
import org.example.serviceadmin.auth.service.PostAuthService;
import org.example.serviceadmin.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证控制器
 * 处理登录相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class PostAuthController {
    private final PostAuthService postAuthService;

    @Autowired
    public PostAuthController(PostAuthService postAuthService) {
        this.postAuthService = postAuthService;
    }

    /**
     * 用户登录接口
     * @param loginRequest 登录请求对象
     * @param request HTTP请求对象
     * @return API响应对象
     */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // 参数校验
        if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty() || loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return new ApiResponse(404, "用户名和密码不能为空");
        }

        // 调用服务层处理登录
        Pair<LoginEnum, Object> result = postAuthService.login(loginRequest, request);
        LoginEnum status = result.getLeft();
        
        // 根据不同状态返回不同响应
        return switch (status) {
            case Success -> new ApiResponse(200, status.getMessage(), result.getRight());
            case AccountLocked -> new ApiResponse(423, status.getMessage(), result.getRight());
            case CaptchaError -> new ApiResponse(400, status.getMessage(), result.getRight());
            default -> new ApiResponse(400, status.getMessage());
        };
    }
}
