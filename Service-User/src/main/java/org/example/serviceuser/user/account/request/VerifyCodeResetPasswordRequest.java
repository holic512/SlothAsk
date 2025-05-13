/**
 * File Name: VerifyCodeResetPasswordRequest.java
 * Description: 验证验证码并重置密码的请求对象
 * Author: holic512
 * Created Date: 2025-05-21
 * Version: 1.0
 * Usage:
 * 用于接收验证验证码并重置密码的请求参数
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCodeResetPasswordRequest {

    /**
     * 唯一标识符，用于获取Redis中存储的验证码信息
     */
    @NotBlank(message = "验证码标识符不能为空")
    private String key;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
} 