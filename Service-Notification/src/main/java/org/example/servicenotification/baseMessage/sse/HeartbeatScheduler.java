/**
 * File Name: HeartbeatScheduler.java
 * Description: SSE心跳定时器，每20秒向所有连接的客户端发送心跳包
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * 自动定时发送心跳包，维持SSE连接活跃状态
 */
package org.example.servicenotification.baseMessage.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartbeatScheduler {

    @Autowired
    private EmitterManager emitterManager;

    /**
     * 每30秒发送一次心跳包
     */
    @Scheduled(fixedRate = 30000)
    public void sendHeartbeat() {
        try {
            String heartbeatMessage = "{\"timestamp\":" + System.currentTimeMillis() + ",\"message\":\"heartbeat\"}";
            emitterManager.sendToAll(SSEMessageType.HEARTBEAT.getValue(), heartbeatMessage);
            log.debug("心跳包发送成功，时间戳: {}", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("发送心跳包时发生异常", e);
        }
    }
}
