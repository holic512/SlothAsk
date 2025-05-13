/**
 * File Name: UpdateEmailWithPasswordRequest.java
 * Description: 通过密码更新邮箱的请求对象
 * Author: holic512
 * Created Date: 2025-05-22
 * Version: 1.0
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 通过密码更新邮箱的请求对象
 */
@Data
public class UpdateEmailWithPasswordRequest {

    /**
     * 唯一标识符，用于从Redis中获取验证信息
     */
    @NotBlank(message = "唯一标识符不能为空")
    private String uid;

    /**
     * 新邮箱地址
     */
    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户密码，用于验证
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 验证码，用于验证邮箱所有权
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
} 