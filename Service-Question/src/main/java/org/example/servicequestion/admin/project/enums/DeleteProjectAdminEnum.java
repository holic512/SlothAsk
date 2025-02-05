/**
 * File Name: DeleteProjectAdminEnum.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-02-03
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.enums;

import lombok.Getter;

@Getter
public enum DeleteProjectAdminEnum {
    SUCCESS("删除项目成功"),
    PROJECT_NOT_FOUND("项目未找到"),
    DELETE_FAILED("删除项目失败"),
    SYSTEM_ERROR("系统错误，请稍后重试"),
    NO_ID_PROVIDED("没有提供删除的项目ID");

    private final String value;

    DeleteProjectAdminEnum(String value) {
        this.value = value;
    }
}
