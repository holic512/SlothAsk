/**
 * File Name: SendAiAnalysisService.java
 * Description: AI解析服务接口，提供发送AI解析请求的功能
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 定义AI解析相关的业务逻辑接口，包括权限验证、状态检查和消息发送
 */
package org.example.servicequestion.user.aiAnalysis.service;

import org.example.servicequestion.user.aiAnalysis.enums.SendAiAnalysisEnum;

public interface SendAiAnalysisService {
    
    /**
     * 发送AI解析请求
     * 
     * @param answerId 回答记录ID
     * @param userId 用户ID
     * @return 操作结果枚举
     */
    SendAiAnalysisEnum sendAiAnalysisRequest(Long answerId, Long userId);
}
