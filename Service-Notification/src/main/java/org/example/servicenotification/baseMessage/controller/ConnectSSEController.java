/**
 * File Name: ConnectSSEController.java
 * Description: SSE连接控制器
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * 提供客户端建立Server-Sent Events连接的接口，供前端通过EventSource访问，实现消息推送功能。
 */

package org.example.servicenotification.baseMessage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.redisKey.BaseMessageKey;
import org.example.servicenotification.baseMessage.sse.EmitterManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("baseMessage")
public class ConnectSSEController {

    private final EmitterManager emitterManager;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * SSE 连接接口
     * GET /sse/connect
     *
     * @param userId 用户ID，通过请求头传递
     * @return SseEmitter实例，用于建立SSE连接
     */
    @GetMapping("/sse/connect")
    public SseEmitter connect(@RequestHeader(value = "X-User-Id") Long userId) {
        log.info("接收到用户 {} 的SSE连接请求", userId);

        // 创建SSE连接
        SseEmitter emitter = emitterManager.createEmitter(userId.toString());
        
        // 将用户ID存储到Redis中标记在线状态，设置5分钟过期时间
        String onlineKey = BaseMessageKey.ONLINE_USER_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(onlineKey, "online", 3, TimeUnit.MINUTES);
        log.info("用户 {} 已标记为在线状态，Redis键: {}", userId, onlineKey);
        
        log.info("用户 {} 的SSE连接创建成功", userId);
        return emitter;
    }
}
