package org.example.servicequestion.admin.project.enums;

import lombok.Getter;

@Getter
public enum GetProjectAdminEnum {
    SUCCESS("查询成功"),
    SEARCH_FAILED("查询失败"),
    SYSTEM_ERROR("系统错误，请稍后重试");

    private final String value;

    GetProjectAdminEnum(String value) {
        this.value = value;
    }
}
