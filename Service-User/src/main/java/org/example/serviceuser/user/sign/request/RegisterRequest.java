/**
 * File Name: RegisterRequest.java
 * Description: 用户注册请求类，用于接收注册请求参数
 * Author: holic512
 * Created Date: 2025-03-11
 * Usage: 用于接收前端提交的注册信息
 */
package org.example.serviceuser.user.sign.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度应在2-20个字符之间")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度应在6-20个字符之间")
    private String password;

    /**
     * 拟注册的uid
     */
    @NotBlank(message = "uid不能为空")
    private String uid;
} 