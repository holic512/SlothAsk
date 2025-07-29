/**
 * File Name: RedisMessageSubscriber.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-27
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.baseMessage.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.entity.BaseMessage;
import org.example.servicenotification.baseMessage.sse.EmitterManager;
import org.example.servicenotification.baseMessage.sse.SSEMessageType;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private final EmitterManager emitterManager;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            // 读取到的 json
            String messageJson = new String(message.getBody(), StandardCharsets.UTF_8);

            // 序列化 信息  为了让获取 用户id
            BaseMessage baseMessage = objectMapper.readValue(messageJson, BaseMessage.class);
            emitterManager.sendToUser(
                    baseMessage.getUserId().toString(),
                    SSEMessageType.BASE_MESSAGE.getValue(),
                    messageJson
            );

            log.info("SSE消息已发送给用户: userId={}, messageId={}",
                    baseMessage.getUserId(), baseMessage.getId());
        } catch (Exception e) {
            log.error("处理Redis广播消息时发生异常: {}", e.getMessage(), e);
        }
    }
}


