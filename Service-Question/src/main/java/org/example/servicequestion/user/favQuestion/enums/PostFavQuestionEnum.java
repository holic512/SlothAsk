/**
 * File Name: PostFavQuestionEnum.java
 * Description: 用户收藏操作的结果枚举，标识收藏或取消收藏操作的成功与否。
 * Author: holic512
 * Created Date: 2025-04-24
 * Version: 1.0
 * Usage:
 * 用于表示用户收藏相关接口的处理结果，包含 SUCCESS（200）与 FAIL（400）两个状态，
 * 可与统一响应体配合使用，提高代码可读性与维护性。
 */

package org.example.servicequestion.user.favQuestion.enums;

import lombok.Getter;

@Getter
public enum PostFavQuestionEnum {
    SUCCESS(200),
    FAIL(400);
    private final int code;

    PostFavQuestionEnum(int code) {
        this.code = code;
    }
}
