/**
 * File Name: GetAnswerQuestionService.java
 * Description: 获取用户答题记录服务接口，提供查询用户答案记录的功能。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 提供用户答题记录查询相关的业务逻辑接口，包括获取答题记录等功能。
 */
package org.example.servicequestion.user.answer.service;

import org.example.servicequestion.user.answer.dto.AnswerRecordResponse;

public interface GetAnswerQuestionService {
    
    /**
     * 获取用户答题记录
     * @param virtualId 问题的虚拟ID
     * @param userId 用户ID
     * @return 答题记录数据，如果不存在则返回null
     */
    AnswerRecordResponse getAnswerRecord(String virtualId, Long userId);
}