/**
 * File Name: JobRecruitPageEnum.java
 * Description: 岗位招聘页面操作结果枚举，标识各种操作的状态
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于表示岗位招聘相关接口的处理结果，包含成功、失败、参数错误等状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */
package org.example.servicejobrecruitpage.user.enums;

import lombok.Getter;

@Getter
public enum JobRecruitPageEnum {
    SUCCESS(200, "操作成功"),
    FAIL(400, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    PARAM_INVALID_USER_ID(400, "用户ID无效"),
    PARAM_INVALID_JOB_ID(400, "岗位ID无效"),
    PARAM_INVALID_PAGE(400, "分页参数无效"),
    PARAM_PAGE_SIZE_EXCEEDED(400, "分页大小超出限制"),
    JOB_NOT_FOUND(404, "岗位不存在"),
    APPLICATION_NOT_FOUND(404, "申请记录不存在"),
    USER_NOT_FOUND(404, "用户不存在"),
    DUPLICATE_APPLICATION(409, "重复申请"),
    APPLICATION_EXPIRED(410, "申请已过期"),
    INVALID_STATUS(400, "无效的申请状态"),
    PERMISSION_DENIED(403, "权限不足"),
    QUERY_FAILED(500, "查询失败"),
    UPDATE_FAILED(500, "更新失败"),
    DATABASE_ERROR(500, "数据库访问异常"),
    SYSTEM_ERROR(500, "系统错误");

    private final int code;
    private final String message;

    JobRecruitPageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}