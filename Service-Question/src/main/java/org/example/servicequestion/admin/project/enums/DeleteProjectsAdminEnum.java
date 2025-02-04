package org.example.servicequestion.admin.project.enums;

import lombok.Getter;

@Getter
public enum DeleteProjectsAdminEnum {
    SUCCESS("批量删除项目成功"),
    PROJECT_NOT_FOUND("项目未找到"),
    DELETE_FAILED("批量删除项目失败"),
    SYSTEM_ERROR("系统错误，请稍后重试"),
    NO_IDS_PROVIDED("没有提供删除的项目ID");

    private final String value;

    DeleteProjectsAdminEnum(String value) {
        this.value = value;
    }
}
