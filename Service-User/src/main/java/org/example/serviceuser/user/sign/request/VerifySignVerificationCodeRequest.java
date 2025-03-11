/**
 * File Name: VerifySignVerificationCodeRequest.java
 * Description: 验证码验证请求类，用于接收验证码验证的请求参数
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * 用于接收前端提交的邮箱和验证码信息，进行验证码验证
 */
package org.example.serviceuser.user.sign.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证码验证请求类
 * 包含验证码验证所需的邮箱和验证码字段
 */
@Data
public class VerifySignVerificationCodeRequest {
    /**
     * 用户邮箱
     * 不能为空，用于查找对应的验证码和用户信息
     */
    @NotBlank
    private String email;
    
    /**
     * 验证码
     * 不能为空，用于与系统生成的验证码进行比对
     */
    @NotBlank
    private String code;
}
