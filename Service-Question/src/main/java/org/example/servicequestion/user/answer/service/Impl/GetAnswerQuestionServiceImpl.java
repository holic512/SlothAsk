/**
 * File Name: GetAnswerQuestionServiceImpl.java
 * Description: 获取用户答题记录服务实现类，提供查询用户答案记录的功能。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 实现了 GetAnswerQuestionService 接口，提供用户答题记录的查询操作。
 */
package org.example.servicequestion.user.answer.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.servicequestion.entity.UserQuestionRecord;
import org.example.servicequestion.user.answer.dto.AnswerRecordResponse;
import org.example.servicequestion.user.answer.mapper.AnswerUserQuestionRecordMapper;
import org.example.servicequestion.user.answer.service.GetAnswerQuestionService;
import org.example.servicequestion.user.commonService.IdConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAnswerQuestionServiceImpl implements GetAnswerQuestionService {

    private final IdConversionService idConversionService;
    private final AnswerUserQuestionRecordMapper answerUserQuestionRecordMapper;

    @Autowired
    public GetAnswerQuestionServiceImpl(IdConversionService idConversionService,
                                        AnswerUserQuestionRecordMapper answerUserQuestionRecordMapper) {
        this.idConversionService = idConversionService;
        this.answerUserQuestionRecordMapper = answerUserQuestionRecordMapper;
    }

    @Override
    public AnswerRecordResponse getAnswerRecord(String virtualId, Long userId) {
        try {
            // 参数校验
            if (virtualId == null || userId == null || virtualId.isEmpty() || userId <= 0) {
                return null;
            }

            // 1. 解析问题ID
            Long realQuestionId = idConversionService.getOriginalIdFromVirtualId(virtualId);
            if (realQuestionId == null) {
                return null;
            }

            // 2. 查询数据库中的答题记录
            QueryWrapper<UserQuestionRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("question_id", realQuestionId);
            
            UserQuestionRecord record = answerUserQuestionRecordMapper.selectOne(queryWrapper);
            
            if (record == null) {
                return null;
            }

            // 3. 构建响应对象
            AnswerRecordResponse response = new AnswerRecordResponse();
            response.setId(record.getId());
            response.setAnswer(record.getUserAnswer());
            response.setIsSubmitted(record.getIsSubmitted());
            response.setUpdateTime(record.getUpdateTime());
            
            return response;
            
        } catch (Exception e) {
            return null;
        }
    }
}