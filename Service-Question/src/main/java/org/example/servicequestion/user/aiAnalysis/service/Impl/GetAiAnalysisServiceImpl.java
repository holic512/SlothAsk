/**
 * File Name: GetAiAnalysisServiceImpl.java
 * Description: 获取AI解析记录服务实现类，提供查询AI解析记录的功能
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 实现获取AI解析记录服务接口，包括权限验证、Redis状态检查和数据库记录查询
 */
package org.example.servicequestion.user.aiAnalysis.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.UserAnswerAiAnalysis;
import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.entity.UserQuestionRecord;
import org.example.servicequestion.user.aiAnalysis.enums.GetAiAnalysisEnum;
import org.example.servicequestion.user.aiAnalysis.mapper.AiAnalysisUAAiAnalysisMapper;
import org.example.servicequestion.user.aiAnalysis.mapper.AiAnalysisUQRecordMapper;
import org.example.servicequestion.user.aiAnalysis.service.GetAiAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetAiAnalysisServiceImpl implements GetAiAnalysisService {

    private final AiAnalysisUQRecordMapper aiAnalysisUQRecordMapper;
    private final AiAnalysisUAAiAnalysisMapper aiAnalysisUAAiAnalysisMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GetAiAnalysisServiceImpl(AiAnalysisUQRecordMapper aiAnalysisUQRecordMapper,
                                  AiAnalysisUAAiAnalysisMapper aiAnalysisUAAiAnalysisMapper,
                                  RedisTemplate<String, Object> redisTemplate) {
        this.aiAnalysisUQRecordMapper = aiAnalysisUQRecordMapper;
        this.aiAnalysisUAAiAnalysisMapper = aiAnalysisUAAiAnalysisMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GetAiAnalysisResult getAiAnalysisRecord(Long answerId, Long userId) {
        try {
            // 参数校验
            if (answerId == null || userId == null || answerId <= 0 || userId <= 0) {
                return new GetAiAnalysisResult(GetAiAnalysisEnum.FAIL);
            }

            // 1. 根据回答ID查询用户回答记录
            UserQuestionRecord record = aiAnalysisUQRecordMapper.selectById(answerId);
            if (record == null) {
                return new GetAiAnalysisResult(GetAiAnalysisEnum.ANSWER_NOT_FOUND);
            }

            // 2. 校验是否是本人的回答
            if (!record.getUserId().equals(userId)) {
                return new GetAiAnalysisResult(GetAiAnalysisEnum.UNAUTHORIZED);
            }

            // 3. 检查是否正在进行AI解析
            String parsingKey = RedisKey.ANSWER_AI_PARSING_KEY + answerId;
            Boolean isExists = redisTemplate.hasKey(parsingKey);
            if (Boolean.TRUE.equals(isExists)) {
                return new GetAiAnalysisResult(GetAiAnalysisEnum.PARSING_IN_PROGRESS);
            }

            // 4. 查询AI解析记录（排除逻辑删除的记录）
            QueryWrapper<UserAnswerAiAnalysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("record_id", answerId)
                       .eq("is_deleted", 0); // 排除逻辑删除的记录
            
            UserAnswerAiAnalysis aiAnalysis = aiAnalysisUAAiAnalysisMapper.selectOne(queryWrapper);
            
            if (aiAnalysis == null) {
                return new GetAiAnalysisResult(GetAiAnalysisEnum.NO_ANALYSIS_RECORD);
            }

            return new GetAiAnalysisResult(GetAiAnalysisEnum.SUCCESS, aiAnalysis);

        } catch (Exception e) {
            return new GetAiAnalysisResult(GetAiAnalysisEnum.FAIL);
        }
    }
}