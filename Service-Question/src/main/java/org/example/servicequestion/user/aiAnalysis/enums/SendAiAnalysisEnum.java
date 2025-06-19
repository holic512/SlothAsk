/**
 * File Name: SendAiAnalysisEnum.java
 * Description: AI解析操作的结果枚举，标识AI解析操作的各种状态。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用于表示AI解析相关接口的处理结果，包含成功、失败、正在解析、无权限等状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */

package org.example.servicequestion.user.aiAnalysis.enums;

import lombok.Getter;

@Getter
public enum SendAiAnalysisEnum {
    SUCCESS(200, "AI解析请求发送成功"),
    FAIL(400, "操作失败"),
    ANSWER_NOT_FOUND(404, "回答记录不存在"),
    UNAUTHORIZED(403, "无权限操作此回答"),
    ALREADY_PARSING(409, "该回答正在进行AI解析，请稍后再试"),
    NOT_SUBMITTED(400, "回答尚未提交，无法进行AI解析"),
    ALREADY_ANALYZED(409, "该问题已经存在AI解析记录");

    private final int code;
    private final String message;

    SendAiAnalysisEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}