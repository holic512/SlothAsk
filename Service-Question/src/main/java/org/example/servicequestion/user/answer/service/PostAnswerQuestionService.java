/**
 * File Name: PostAnswerQuestionService.java
 * Description: 用户答题服务接口，提供保存用户答案的功能。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 提供用户答题相关的业务逻辑接口，包括保存答案、检查提交状态等功能。
 */
package org.example.servicequestion.user.answer.service;

import org.example.servicequestion.user.answer.enums.PostAnswerQuestionEnum;

public interface PostAnswerQuestionService {
    
    /**
     * 保存用户答案
     * @param virtualId 问题的虚拟ID
     * @param userId 用户ID
     * @param answer 用户答案
     * @return 操作结果枚举
     */
    PostAnswerQuestionEnum saveAnswer(String virtualId, Long userId, String answer);
    
    /**
     * 提交用户答案
     * @param answerId 回答记录ID
     * @param userId 用户ID
     * @return 操作结果枚举
     */
    PostAnswerQuestionEnum submitAnswer(Long answerId, Long userId);
}
