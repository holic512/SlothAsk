package org.example.servicequestion.config.HistoryQueueConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 历史记录队列配置类
 */
@Configuration
public class HistoryQueueConfig {
    
    /**
     * 定义历史记录队列的最大容量
     */
    private static final int QUEUE_CAPACITY = 1000;
    
    /**
     * 创建并配置历史记录队列Bean
     *
     * @return 历史记录队列
     */
    @Bean
    public BlockingQueue<HistoryRecord> historyRecordQueue() {
        return new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }
} 