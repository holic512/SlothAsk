/**
 * File Name: UpdateEmailWithTwoStepsRequest.java
 * Description: 双重验证更新邮箱的请求对象
 * Author: holic512
 * Created Date: 2025-05-22
 * Version: 1.0
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 双重验证更新邮箱的请求对象
 * 需要同时验证原邮箱和新邮箱的所有权
 */
@Data
public class UpdateEmailWithTwoStepsRequest {

    /**
     * 原邮箱的唯一标识符
     */
    @NotBlank(message = "原邮箱的唯一标识符不能为空")
    private String originalUid;

    /**
     * 原邮箱的验证码
     */
    @NotBlank(message = "原邮箱的验证码不能为空")
    private String originalCode;

    /**
     * 新邮箱地址
     */
    @NotBlank(message = "新邮箱地址不能为空")
    @Email(message = "新邮箱格式不正确")
    private String newEmail;

    /**
     * 新邮箱的唯一标识符
     */
    @NotBlank(message = "新邮箱的唯一标识符不能为空")
    private String newUid;

    /**
     * 新邮箱的验证码
     */
    @NotBlank(message = "新邮箱的验证码不能为空")
    private String newCode;
} 