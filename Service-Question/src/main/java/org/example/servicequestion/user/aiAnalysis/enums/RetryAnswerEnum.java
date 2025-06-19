/**
 * File Name: RetryAnswerEnum.java
 * Description: 重新回答操作的结果枚举，标识重新回答操作的各种状态。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用于表示重新回答相关接口的处理结果，包含成功、失败、正在解析、无权限等状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */

package org.example.servicequestion.user.aiAnalysis.enums;

import lombok.Getter;

@Getter
public enum RetryAnswerEnum {
    SUCCESS(200, "重新回答设置成功"),
    FAIL(400, "操作失败"),
    ANSWER_NOT_FOUND(404, "回答记录不存在"),
    UNAUTHORIZED(403, "无权限操作此回答"),
    AI_PARSING_IN_PROGRESS(409, "该问题正在被AI解析，请先等待解析完成后再重试");

    private final int code;
    private final String message;

    RetryAnswerEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}