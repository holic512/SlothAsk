/**
 * File Name: PutUserAdminEnum.java
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
public enum PutUserAdminEnum {
    SUCCESS("成功"),
    FAILURE("失败");

    private final String message;

    PutUserAdminEnum(String message) {
        this.message = message;
    }
}
