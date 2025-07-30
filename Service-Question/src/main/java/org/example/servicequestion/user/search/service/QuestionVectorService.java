/**
 * File Name: QuestionVectorService.java
 * Description: 题目向量化服务接口
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * 提供题目向量化相关的服务方法
 */
package org.example.servicequestion.user.search.service;

import java.time.LocalDateTime;

public interface QuestionVectorService {
    
    /**
     * 定时任务：向量化更新的题目
     * 查询上次更新时间后更改的内容，批量向量化并存储
     */
    void scheduleVectorizeUpdatedQuestions();
    
    /**
     * 手动批量全部向量化
     * 异步处理，覆盖式更新，有锁防止重复执行
     * 
     * @return 是否成功启动向量化任务
     */
    boolean manualBatchVectorizeAllQuestions();
    
    /**
     * 获取向量化进度
     * 
     * @return 进度信息（百分比）
     */
    String getVectorizationProgress();
    
    /**
     * 批量向量化指定时间后更新的题目
     * 
     * @param lastUpdateTime 最后更新时间
     * @return 处理的题目数量
     */
    int vectorizeQuestionsAfter(LocalDateTime lastUpdateTime);
    
    /**
     * 批量向量化所有题目
     * 
     * @return 处理的题目数量
     */
    int vectorizeAllQuestions();
    
    /**
     * 相似度查询
     * 根据输入文本查询相似的题目
     * 
     * @param queryText 查询文本
     * @return 相似题目列表
     */
    java.util.List<org.example.servicequestion.config.Qdrant.QdrantVectorService.SimilarityResult> searchSimilarQuestions(String queryText);
}