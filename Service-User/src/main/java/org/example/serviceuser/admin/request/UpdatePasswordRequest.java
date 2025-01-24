/**
 * File Name: UpdatePasswordRequest.java
 * Description: 更新用户密码请求数据传输对象
 * Author: holic512
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 用于接收管理员更新用户密码时的请求数据，包含用户ID和新密码，并进行数据校验
 */
package org.example.serviceuser.admin.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    @Min(value = 1,message = "id不能为空")
    Long id;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6到20个字符之间")
    String newPassword;
}
