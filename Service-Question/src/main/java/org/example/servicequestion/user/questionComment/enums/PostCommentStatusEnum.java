/**
 * File Name: PostCommentStatusEnum.java
 * Description: 评论状态枚举
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.enums;

import lombok.Getter;

/**
 * 发表评论操作状态枚举
 */
@Getter
public enum PostCommentStatusEnum {

    SUCCESS(200, "操作成功"),
    USER_NOT_LOGGED_IN(401, "用户未登录"),
    EMPTY_CONTENT(402, "评论内容不能为空"),
    INVALID_TARGET_ID(403, "目标ID无效"),
    PERMISSION_DENIED(405, "权限不足，无法删除他人评论"),
    COMMENT_NOT_FOUND(404, "评论不存在"),
    SYSTEM_ERROR(500, "系统错误");

    private final int code;
    private final String message;

    PostCommentStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}