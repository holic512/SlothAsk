/**
 * File Name: VerifyAndUpdateEmailRequest.java
 * Description: 邮箱验证和更新请求类
 * Author: holic512
 * Created Date: 2025-05-25
 * Version: 1.0
 * Usage:
 * 用于接收邮箱验证和更新的请求数据
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 邮箱验证和更新请求类
 */
@Data
public class VerifyAndUpdateEmailRequest {

    /**
     * 唯一标识符 (由发送验证码接口返回)
     */
    @NotBlank(message = "唯一标识符不能为空")
    private String uid;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须是6位")
    private String code;

    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
} 