/**
 * File Name: SSEMessageType.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.baseMessage.sse;

import lombok.Getter;

@Getter
public enum SSEMessageType {
    HEARTBEAT("heartbeat"),
    BASE_MESSAGE("basemessage");

    private final String value;

    SSEMessageType(String value) {
        this.value = value;
    }

    // 可选：根据字符串获取枚举
    public static SSEMessageType fromValue(String value) {
        for (SSEMessageType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知消息类型: " + value);
    }
}
