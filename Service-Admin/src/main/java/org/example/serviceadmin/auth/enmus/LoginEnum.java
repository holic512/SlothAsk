/**
 * File Name: LoginEnum.java
 * Description: 登录相关的枚举类型
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 */
package org.example.serviceadmin.auth.enmus;

/**
 * 登录状态枚举
 */
public enum LoginEnum {
    Success("登录成功"),
    UserNotFound("用户不存在"),
    PasswordError("密码错误"),
    AccountLocked("账户已锁定，请完成验证码验证"),
    CaptchaError("验证码错误");

    private final String message;

    LoginEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
