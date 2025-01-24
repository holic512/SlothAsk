/**
 * File Name: PutUserAdminEnum.java
 * Description: 用户更新操作结果枚举类
 * Author: holic512
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 定义用户信息更新操作的可能结果状态，包括成功、失败、邮箱已存在、用户名已存在、手机号已存在等
 */
package org.example.serviceuser.admin.enums;

import lombok.Getter;

@Getter
public enum PutUserAdminEnum {
    SUCCESS("成功"),
    ALREADY_EMAIL("已有邮箱"),
    ALREADY_USERNAME("已有用户名"),
    ALREADY_PHONE("已有手机号"),
    FAILURE("失败");

    private final String message;

    PutUserAdminEnum(String message) {
        this.message = message;
    }
}
