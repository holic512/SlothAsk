/**
 * File Name: PostEnum.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.enums;

import lombok.Getter;

@Getter
public enum PostUserAdminEnum {
    SUCCESS("成功"),
    ALREADY_EMAIL("已有邮箱"),
    ALREADY_USERNAME("已有用户名"),
    ALREADY_PHONE("已有手机号");

    private final String message;

    PostUserAdminEnum(String message) {
        this.message = message;
    }
}

