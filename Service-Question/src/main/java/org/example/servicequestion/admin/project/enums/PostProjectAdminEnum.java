/**
 * File Name: PostProjectAdminEnum.java
 * Description: 项目管理相关操作结果枚举
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 */
package org.example.servicequestion.admin.project.enums;

import lombok.Getter;

@Getter
public enum PostProjectAdminEnum {
    SUCCESS("添加项目成功"),
    SAVE_FAILED("添加项目失败"),
    SYSTEM_ERROR("系统错误，请稍后重试");

    private final String value;
    
    PostProjectAdminEnum(String value) {
        this.value = value;
    }
}
