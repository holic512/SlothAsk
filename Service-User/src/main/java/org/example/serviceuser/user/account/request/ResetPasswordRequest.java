/**
 * File Name: ResetPasswordRequest.java
 * Description: 重置密码的请求类
 * Author: holic512
 * Created Date: 2025-08-30
 * Version: 1.0
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    
    @NotBlank(message = "唯一标识符不能为空")
    private String key;
    
    @NotBlank(message = "验证码不能为空")
    private String code;
    
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
} 