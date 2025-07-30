/**
 * File Name: QuestionVectorScheduleTask.java
 * Description: 题目向量化定时任务
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * 每天0点执行题目向量化任务
 */
package org.example.servicequestion.user.search.task;

import lombok.extern.slf4j.Slf4j;
import org.example.servicequestion.user.search.service.QuestionVectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuestionVectorScheduleTask {
    
    private final QuestionVectorService questionVectorService;
    
    @Autowired
    public QuestionVectorScheduleTask(QuestionVectorService questionVectorService) {
        this.questionVectorService = questionVectorService;
    }
    
    /**
     * 每天0点执行题目向量化任务
     * cron表达式: 秒 分 时 日 月 周
     * 0 0 0 * * ? 表示每天0点0分0秒执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleVectorizeQuestions() {
        log.info("开始执行定时题目向量化任务");
        
        try {
            questionVectorService.scheduleVectorizeUpdatedQuestions();
            log.info("定时题目向量化任务执行完成");
        } catch (Exception e) {
            log.error("定时题目向量化任务执行失败", e);
        }
    }
}