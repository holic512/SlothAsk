/**
 * File Name: MessageType.java
 * Description: 消息类型常量定义
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 定义系统中各种消息类型的常量值，与前端MessageType枚举保持一致
 */
package org.example.servicecommon.baseMessage.enums;

public class BaseMessageType {

    /**
     * AI识别消息
     */
    public static final Integer AI_RECOGNITION = 1;

    /**
     * 用户关注消息
     */
    public static final Integer USER_FOLLOW = 2;

    /**
     * 评论回复消息
     */
    public static final Integer COMMENT_REPLY = 3;

    // 私有构造函数，防止实例化
    private BaseMessageType() {
    }
}