/**
 * File Name: UpdatePasswordRequest.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
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
