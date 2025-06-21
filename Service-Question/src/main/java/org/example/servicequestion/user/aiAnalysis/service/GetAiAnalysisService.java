/**
 * File Name: GetAiAnalysisService.java
 * Description: 获取AI解析记录服务接口，提供查询AI解析记录的功能
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 定义获取AI解析记录相关的业务逻辑接口，包括权限验证、状态检查和记录查询
 */
package org.example.servicequestion.user.aiAnalysis.service;

import lombok.Getter;
import org.example.servicecommon.entity.UserAnswerAiAnalysis;
import org.example.servicequestion.user.aiAnalysis.enums.GetAiAnalysisEnum;

public interface GetAiAnalysisService {
    
    /**
     * 获取AI解析记录
     * 
     * @param answerId 回答记录ID
     * @param userId 用户ID
     * @return 包含操作结果和AI解析记录的响应对象
     */
    GetAiAnalysisResult getAiAnalysisRecord(Long answerId, Long userId);
    
    /**
     * 获取AI解析结果的封装类
     */
    @Getter
    class GetAiAnalysisResult {
        private GetAiAnalysisEnum status;
        private UserAnswerAiAnalysis data;
        
        public GetAiAnalysisResult(GetAiAnalysisEnum status) {
            this.status = status;
        }
        
        public GetAiAnalysisResult(GetAiAnalysisEnum status, UserAnswerAiAnalysis data) {
            this.status = status;
            this.data = data;
        }

    }
}