/**
 * File Name: InitializePasswordRequest.java
 * Description: 初始化密码请求类
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 用于接收初始化密码请求的数据
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InitializePasswordRequest {
    
    /**
     * 新密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
} 