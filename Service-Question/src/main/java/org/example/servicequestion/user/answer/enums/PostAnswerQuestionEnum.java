/**
 * File Name: PostAnswerQuestionEnum.java
 * Description: 用户答题操作的结果枚举，标识答题操作的各种状态。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 用于表示用户答题相关接口的处理结果，包含成功、失败、已提交等状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */

package org.example.servicequestion.user.answer.enums;

import lombok.Getter;

@Getter
public enum PostAnswerQuestionEnum {
    SUCCESS(200, "操作成功"),
    FAIL(400, "操作失败"),
    ALREADY_SUBMITTED(409, "问题已经提交，无法修改"),
    QUESTION_NOT_FOUND(404, "问题不存在"),
    ANSWER_NOT_FOUND(404, "回答记录不存在"),
    UNAUTHORIZED(403, "无权限操作此回答");

    private final int code;
    private final String message;

    PostAnswerQuestionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}