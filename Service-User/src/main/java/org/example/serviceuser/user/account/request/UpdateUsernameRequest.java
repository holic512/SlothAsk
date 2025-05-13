/**
 * File Name: UpdateUsernameRequest.java
 * Description: 更新用户名请求类
 * Author: holic512
 * Created Date: 2025-05-20
 * Version: 1.0
 * Usage:
 * 用于接收用户名更新请求的数据
 */
package org.example.serviceuser.user.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUsernameRequest {
    
    /**
     * 新用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
}