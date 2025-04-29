package org.example.servicequestion.config.HistoryQueueConfig;

/**
 * 历史记录数据模型，用于在队列中传输
 */
public record HistoryRecord(Long userId, Long questionId) {
}