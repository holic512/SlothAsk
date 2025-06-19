/**
 * File Name: RetryAnswerService.java
 * Description: 重新回答服务接口，提供重新回答的功能
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 定义重新回答相关的业务逻辑接口，包括权限验证、状态检查和重置回答状态
 */
package org.example.servicequestion.user.aiAnalysis.service;

import org.example.servicequestion.user.aiAnalysis.enums.RetryAnswerEnum;

public interface RetryAnswerService {
    
    /**
     * 重新回答
     * 
     * @param answerId 回答记录ID
     * @param userId 用户ID
     * @return 操作结果枚举
     */
    RetryAnswerEnum retryAnswer(Long answerId, Long userId);
}