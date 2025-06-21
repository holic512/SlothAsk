/**
 * File Name: RetryAnswerServiceImpl.java
 * Description: 重新回答服务实现类，提供重新回答的功能
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 实现重新回答服务接口，包括权限验证、Redis状态检查、修改提交状态和伪删除AI解析记录
 */
package org.example.servicequestion.user.aiAnalysis.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.example.servicecommon.entity.UserAnswerAiAnalysis;
import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.entity.UserQuestionRecord;
import org.example.servicequestion.user.aiAnalysis.enums.RetryAnswerEnum;
import org.example.servicequestion.user.aiAnalysis.mapper.AiAnalysisUAAiAnalysisMapper;
import org.example.servicequestion.user.aiAnalysis.mapper.AiAnalysisUQRecordMapper;
import org.example.servicequestion.user.aiAnalysis.service.RetryAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetryAnswerServiceImpl implements RetryAnswerService {

    private final AiAnalysisUQRecordMapper aiAnalysisUQRecordMapper;
    private final AiAnalysisUAAiAnalysisMapper aiAnalysisUAAiAnalysisMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RetryAnswerServiceImpl(AiAnalysisUQRecordMapper aiAnalysisUQRecordMapper,
                                AiAnalysisUAAiAnalysisMapper aiAnalysisUAAiAnalysisMapper,
                                RedisTemplate<String, Object> redisTemplate) {
        this.aiAnalysisUQRecordMapper = aiAnalysisUQRecordMapper;
        this.aiAnalysisUAAiAnalysisMapper = aiAnalysisUAAiAnalysisMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public RetryAnswerEnum retryAnswer(Long answerId, Long userId) {
        try {
            // 参数校验
            if (answerId == null || userId == null || answerId <= 0 || userId <= 0) {
                return RetryAnswerEnum.FAIL;
            }

            // 1. 根据回答ID查询记录
            UserQuestionRecord record = aiAnalysisUQRecordMapper.selectById(answerId);
            if (record == null) {
                return RetryAnswerEnum.ANSWER_NOT_FOUND;
            }

            // 2. 校验是否是本人的回答
            if (!record.getUserId().equals(userId)) {
                return RetryAnswerEnum.UNAUTHORIZED;
            }

            // 3. 检查是否正在进行AI解析
            String parsingKey = RedisKey.ANSWER_AI_PARSING_KEY + answerId;
            Boolean isExists = redisTemplate.hasKey(parsingKey);
            if (Boolean.TRUE.equals(isExists)) {
                return RetryAnswerEnum.AI_PARSING_IN_PROGRESS;
            }

            // 4. 将该回答的提交状态改成未提交
            UpdateWrapper<UserQuestionRecord> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", answerId)
                        .set("is_submitted", 0);
            aiAnalysisUQRecordMapper.update(null, updateWrapper);

            // 5. 查询是否存在未被伪删除的AI解析记录
            QueryWrapper<UserAnswerAiAnalysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("record_id", answerId)
                       .eq("is_deleted", 0); // 查询未被伪删除的记录
            
            UserAnswerAiAnalysis aiAnalysis = aiAnalysisUAAiAnalysisMapper.selectOne(queryWrapper);
            
            // 6. 如果存在未被伪删除的AI解析记录，则将其伪删除
            if (aiAnalysis != null) {
                UpdateWrapper<UserAnswerAiAnalysis> aiUpdateWrapper = new UpdateWrapper<>();
                aiUpdateWrapper.eq("record_id", answerId)
                              .eq("is_deleted", 0)
                              .set("is_deleted", 1);
                aiAnalysisUAAiAnalysisMapper.update(null, aiUpdateWrapper);
            }

            return RetryAnswerEnum.SUCCESS;

        } catch (Exception e) {
            return RetryAnswerEnum.FAIL;
        }
    }
}