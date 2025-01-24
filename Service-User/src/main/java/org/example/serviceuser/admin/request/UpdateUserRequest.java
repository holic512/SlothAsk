/**
 * File Name: UpdateUserRequest.java
 * Description: 更新用户信息请求数据传输对象
 * Author: holic512
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 用于接收管理员更新用户基本信息时的请求数据，包含用户ID和需要更新的信息字段，并进行数据校验
 */
package org.example.serviceuser.admin.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Min(value = 1, message = "id不能为空")
    Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotNull(message = "性别不能为空")
    private Integer gender;

    private Integer age;

    private String avatar;

    private String bio;
}
