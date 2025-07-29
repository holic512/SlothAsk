/**
 * File Name: RedisMessagePublisher.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-27
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.baseMessage.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessagePublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic topic;

    public void publish(String message) {
        try {
            stringRedisTemplate.convertAndSend(topic.getTopic(), message);
            log.info("消息已发布到Redis频道: {}", topic.getTopic());
        } catch (Exception e) {
            log.error("发布消息到Redis失败: {}", e.getMessage(), e);
            throw new RuntimeException("Redis消息发布失败", e);
        }
    }
}
