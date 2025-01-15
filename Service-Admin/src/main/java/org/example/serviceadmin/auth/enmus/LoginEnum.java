/**
 * File Name: LoginEnum.java
 * Description: 登录相关的枚举类型
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 */
package org.example.serviceadmin.auth.enmus;

import lombok.Getter;

@Getter
public enum LoginEnum {
    Success("登录成功"),
    UserNotFound("用户名不存在"),
    PasswordError("密码错误");

    private final String message;

    // 构造函数
    LoginEnum(String message) {
        this.message = message;
    }

}
