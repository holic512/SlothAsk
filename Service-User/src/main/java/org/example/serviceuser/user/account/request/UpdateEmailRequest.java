/**
 * File Name: UpdateEmailRequest.java
 * Description: 更新邮箱请求类
 * Author: holic512
 * Created Date: 2025-05-20
 * Version: 1.0
 * Usage:
 * 用于接收邮箱更新请求的数据
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmailRequest {
    
    /**
     * 新邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}