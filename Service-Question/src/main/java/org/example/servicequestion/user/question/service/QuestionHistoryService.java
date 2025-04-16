package org.example.servicequestion.user.question.service;

/**
 * 问题历史记录服务接口
 */
public interface QuestionHistoryService {
    
    /**
     * 添加用户访问问题记录到队列
     *
     * @param userId     用户ID
     * @param questionId 问题ID
     */
    void addHistoryRecord(Long userId, Long questionId);
    
    /**
     * 批量保存队列中的历史记录到数据库
     */
    void saveHistoryRecordBatch();
} 