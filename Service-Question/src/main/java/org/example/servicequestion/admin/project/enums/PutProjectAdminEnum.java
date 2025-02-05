package org.example.servicequestion.admin.project.enums;

import lombok.Getter;

@Getter
public enum PutProjectAdminEnum {
    SUCCESS("修改项目成功"),
    UPDATE_FAILED("修改项目失败"),
    PROJECT_NOT_FOUND("项目不存在"),
    SYSTEM_ERROR("系统错误，请稍后重试");

    private final String value;

    PutProjectAdminEnum(String value) {
        this.value = value;
    }
}
