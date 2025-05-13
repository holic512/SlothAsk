/**
 * File Name: VerifyGithubBindRequest.java
 * Description: GitHub绑定验证请求对象
 * Author: holic512
 * Created Date: 2025-05-22
 * Version: 1.0
 * Usage:
 * 用于接收验证GitHub绑定验证码的请求参数
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * GitHub绑定验证请求对象
 */
@Data
public class VerifyGithubBindRequest {
    
    /**
     * 唯一标识符
     */
    @NotBlank(message = "唯一标识符不能为空")
    private String uid;
    
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
} 