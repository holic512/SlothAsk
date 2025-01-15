/**
 * File Name: LoginRequest.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.auth.dto;

import lombok.Data;

/**
 * 登录请求对象
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 验证码ID
     */
    private String captchaId;
    
    /**
     * 验证码答案
     */
    private String captchaAnswer;
}