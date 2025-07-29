/**
 * File Name: EmitterManager.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.baseMessage.sse;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.example.servicecommon.redisKey.BaseMessageKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EmitterManager {

    // 存储所有在线用户的 emitter
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 手动添加已有的 emitter
     */
    public void addEmitter(String userId, SseEmitter emitter) {
        emitterMap.put(userId, emitter);
    }

    /**
     * 根据 userId 自动创建 emitter 并注册
     */
    public SseEmitter createEmitter(String userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30分钟有效期

        // 注册清理逻辑
        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> removeEmitter(userId));

        // 添加到映射中
        emitterMap.put(userId, emitter);
        return emitter;
    }


    /**
     * 移除 SSE 连接
     */
    public void removeEmitter(String userId) {
        emitterMap.remove(userId);
    }

    /**
     * 向所有用户广播消息
     */
    public void sendToAll(String eventType, String message) {
        List<String> successfulUserIds = new ArrayList<>();

        // 使用迭代器避免并发修改异常
        emitterMap.entrySet().removeIf(entry -> {
            try {
                entry.getValue().send(SseEmitter.event()
                        .name(eventType)
                        .data(message));
                // 发送成功，记录用户ID用于后续批量更新Redis
                successfulUserIds.add(entry.getKey());
                return false; // 发送成功，保留该连接
            } catch (IOException e) {
                // 发送失败，移除该连接
                System.out.println("用户 " + entry.getKey() + " 连接已断开，移除emitter");
                return true; // 返回true表示移除该元素
            }
        });

        // 批量重置成功发送消息的用户在线状态
        if (!successfulUserIds.isEmpty()) {
            batchResetOnlineStatus(successfulUserIds);
        }
    }

    /**
     * 批量重置用户在线状态
     */
    private void batchResetOnlineStatus(List<String> userIds) {
        try {
            // 使用Redis Pipeline批量操作提高性能
            redisTemplate.executePipelined((org.springframework.data.redis.core.RedisCallback<Object>) connection -> {
                for (String userId : userIds) {
                    String onlineKey = BaseMessageKey.ONLINE_USER_KEY_PREFIX + userId;
                    // 重置过期时间为5分钟
                    redisTemplate.expire(onlineKey, 3, TimeUnit.MINUTES);
                }
                return null;
            });
            System.out.println("批量重置了 " + userIds.size() + " 个用户的在线状态");
        } catch (Exception e) {
            System.err.println("批量重置用户在线状态失败: " + e.getMessage());
        }
    }

    /**
     * 当程序关闭时 自动清空所有的连接
     */
    @PreDestroy
    public void onShutdown() {
        for (SseEmitter emitter : emitterMap.values()) {
            emitter.complete(); // 主动关闭连接
        }
    }

    /**
     * 向指定用户发送消息
     */
    public void sendToUser(String userId, String eventType, String message) {
        SseEmitter emitter = emitterMap.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventType)
                        .data(message));
            } catch (IOException e) {
                // 发送失败，说明连接已断开，移除
                removeEmitter(userId);
            }
        }
    }

    /**
     * 检查用户是否在线
     */
    public boolean isOnline(String userId) {
        return emitterMap.containsKey(userId);
    }
}
