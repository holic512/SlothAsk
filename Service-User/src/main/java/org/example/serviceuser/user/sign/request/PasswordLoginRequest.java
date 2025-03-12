/**
 * File Name: PasswordLoginRequest.java
 * Description: 密码登录请求类，用于接收登录请求参数
 * Author: holic512
 * Created Date: 2025-03-11
 * Usage: 用于接收前端提交的登录信息
 */
package org.example.serviceuser.user.sign.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordLoginRequest {
    /**
     * 账号（邮箱或用户名）
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
} 