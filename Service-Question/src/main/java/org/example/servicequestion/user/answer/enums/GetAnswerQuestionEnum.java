/**
 * File Name: GetAnswerQuestionEnum.java
 * Description: 获取用户答题记录操作的结果枚举，标识查询操作的各种状态。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 用于表示获取用户答题记录相关接口的处理结果，包含成功、失败、记录不存在等状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */

package org.example.servicequestion.user.answer.enums;

import lombok.Getter;

@Getter
public enum GetAnswerQuestionEnum {
    SUCCESS(200, "查询成功"),
    FAIL(400, "查询失败"),
    RECORD_NOT_FOUND(404, "答题记录不存在"),
    QUESTION_NOT_FOUND(404, "问题不存在");

    private final int code;
    private final String message;

    GetAnswerQuestionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}