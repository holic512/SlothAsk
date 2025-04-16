package org.example.servicequestion.config.HistoryQueueConfig;

/**
 * 历史记录数据模型，用于在队列中传输
 */
public class HistoryRecord {
    private final Long userId;
    private final Long questionId;
    
    public HistoryRecord(Long userId, Long questionId) {
        this.userId = userId;
        this.questionId = questionId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getQuestionId() {
        return questionId;
    }
} 