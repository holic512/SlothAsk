/**
 * File Name: UpdatePasswordRequest.java
 * Description: 更新密码的请求类
 * Author: holic512
 * Created Date: 2025-05-25
 * Version: 1.0
 * Usage:
 * 用于接收用户修改密码的请求数据
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
} 