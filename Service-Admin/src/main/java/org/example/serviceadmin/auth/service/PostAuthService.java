/**
 * File Name: PostAuthService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.auth.service;

import org.apache.commons.lang3.tuple.Pair;
import org.example.serviceadmin.auth.dto.LoginRequest;
import org.example.serviceadmin.auth.enmus.LoginEnum;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证服务接口
 */
public interface PostAuthService {
    /**
     * 用户登录
     * @param loginRequest 登录请求对象
     * @param request HTTP请求对象
     * @return Pair对象，包含登录状态和返回数据（可能是token信息或验证码信息）
     */
    Pair<LoginEnum, Object> login(LoginRequest loginRequest, HttpServletRequest request);
}
