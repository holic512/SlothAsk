/**
 * File Name: MessageBuffer.java
 * Description: 本地消息缓冲区组件，负责暂存 RabbitMQ 消息，实现基于数量或超时的批量触发机制，并支持消息确认（ACK）操作。
 * Author: holic512
 * Created Date: 2025-04-28
 * Version: 1.0
 * Usage:
 * 消费者接收消息后调用 add 方法将消息放入缓冲区；业务逻辑可周期性调用 flushIfNeeded 方法判断是否满足处理条件；
 * 当处理完成后可通过 basicAck 进行批量确认，确保 RabbitMQ 消息消费成功。
 */

package org.example.servicequestion.user.favQuestion.consumer;

import com.rabbitmq.client.Channel;
import lombok.Data;
import org.example.servicequestion.user.favQuestion.dto.FavoriteMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本地消息缓冲区，负责存储接收到的消息并管理批量/超时触发
 */
@Component
@Data
public class MessageBuffer {
    // 缓冲消息列表
    private final ArrayBlockingQueue<FavoriteMessage> buffer = new ArrayBlockingQueue<>(1000);
    // 上次刷新时间，用于超时判断
    private volatile long lastFlushTime = System.currentTimeMillis();

    private volatile long deliveryTag;
    private volatile Channel channel;
    private volatile Lock lock = new ReentrantLock();

    public void setDeliveryTagAndChannel(long deliveryTag, Channel channel) {
        try {
            lock.lock();
            this.deliveryTag = deliveryTag;
            this.channel = channel;
        } finally {
            lock.unlock();
        }
    }

    public void basicAck() throws IOException {
        try {
            lock.lock();
            if (deliveryTag != 0 && channel != null)
                channel.basicAck(deliveryTag, true); // ⭐ multiple=true
        } finally {
            lock.unlock();
        }
    }

    /**
     * 接收消息并加入缓冲
     */
    public void add(FavoriteMessage msg) throws InterruptedException {
        buffer.put(msg);
    }

    /**
     * 判断是否需要刷新（达到批量或超时），并返回待处理列表
     *
     * @param batchSize 批量大小阈值
     * @param timeoutMs 超时时长（毫秒）
     * @return 待刷新的消息列表，若不满足条件则返回空列表
     */
    public List<FavoriteMessage> flushIfNeeded(int batchSize, long timeoutMs) {

        long now = System.currentTimeMillis();
        // 满足批量大小 或 超时且缓冲非空
        if (buffer.size() >= batchSize || (now - lastFlushTime >= timeoutMs && !buffer.isEmpty())) {
            List<FavoriteMessage> toFlush = new ArrayList<>();
            buffer.drainTo(toFlush);
            lastFlushTime = now;
            return toFlush;
        }
        return Collections.emptyList();
    }
}
