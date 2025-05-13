/**
 * File Name: SendPasswordResetCodeRequest.java
 * Description: 发送密码重置验证码的请求类
 * Author: holic512
 * Created Date: 2025-08-30
 * Version: 1.0
 */
package org.example.serviceuser.user.account.request;

import lombok.Data;

@Data
public class SendPasswordResetCodeRequest {
    // 此请求不需要额外参数，用户ID从请求头获取
} 