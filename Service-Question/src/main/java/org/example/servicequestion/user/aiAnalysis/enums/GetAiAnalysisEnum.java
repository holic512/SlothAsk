/**
 * File Name: GetAiAnalysisEnum.java
 * Description: 获取AI解析记录操作的结果枚举，标识获取AI解析记录操作的各种状态。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用于表示获取AI解析记录相关接口的处理结果，包含成功、失败、正在解析、无记录等状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */

package org.example.servicequestion.user.aiAnalysis.enums;

import lombok.Getter;

@Getter
public enum GetAiAnalysisEnum {
    SUCCESS(200, "获取AI解析记录成功"),
    FAIL(400, "操作失败"),
    ANSWER_NOT_FOUND(404, "回答记录不存在"),
    UNAUTHORIZED(403, "无权限查看此回答的AI解析"),
    PARSING_IN_PROGRESS(408, "AI解析正在进行中，请稍后查询"),
    NO_ANALYSIS_RECORD(409, "该回答还没有被AI解析过");

    private final int code;
    private final String message;

    GetAiAnalysisEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}