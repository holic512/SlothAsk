/**
 * File Name: PostAnswerQuestionServiceImpl.java
 * Description: 用户答题服务实现类，提供保存用户答案的功能。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 实现了 PostAnswerQuestionService 接口，提供用户答题的保存操作，支持检查提交状态和更新答案。
 */
package org.example.servicequestion.user.answer.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.servicecommon.redisKey.AnswerAiKey;
import org.example.servicequestion.entity.UserQuestionRecord;
import org.example.servicequestion.user.answer.enums.PostAnswerQuestionEnum;
import org.example.servicequestion.user.answer.mapper.AnswerUserQuestionRecordMapper;
import org.example.servicequestion.user.answer.service.PostAnswerQuestionService;
import org.example.servicequestion.user.commonService.IdConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class PostAnswerQuestionServiceImpl implements PostAnswerQuestionService {

    private final IdConversionService idConversionService;
    private final AnswerUserQuestionRecordMapper answerUserQuestionRecordMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PostAnswerQuestionServiceImpl(IdConversionService idConversionService,
                                         AnswerUserQuestionRecordMapper answerUserQuestionRecordMapper,
                                         RedisTemplate<String, Object> redisTemplate) {
        this.idConversionService = idConversionService;
        this.answerUserQuestionRecordMapper = answerUserQuestionRecordMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PostAnswerQuestionEnum saveAnswer(String virtualId, Long userId, String answer) {
        try {
            // 参数校验
            if (virtualId == null || userId == null || answer == null || 
                virtualId.isEmpty() || answer.isEmpty() || userId <= 0) {
                return PostAnswerQuestionEnum.FAIL;
            }

            // 1. 解析问题ID
            Long realQuestionId = idConversionService.getOriginalIdFromVirtualId(virtualId);
            if (realQuestionId == null) {
                return PostAnswerQuestionEnum.QUESTION_NOT_FOUND;
            }

            // 2. 查询数据库中是否已有该用户对该问题的记录
            QueryWrapper<UserQuestionRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("question_id", realQuestionId);
            
            UserQuestionRecord existingRecord = answerUserQuestionRecordMapper.selectOne(queryWrapper);

            if (existingRecord != null) {
                // 3. 如果记录存在，检查是否已提交
                if (existingRecord.getIsSubmitted() != null && existingRecord.getIsSubmitted() == 1) {
                    return PostAnswerQuestionEnum.ALREADY_SUBMITTED;
                }
                
                // 4. 如果未提交，更新答案
                existingRecord.setUserAnswer(answer);
                existingRecord.setUpdateTime(LocalDateTime.now());
                int updateResult = answerUserQuestionRecordMapper.updateById(existingRecord);
                
                return updateResult > 0 ? PostAnswerQuestionEnum.SUCCESS : PostAnswerQuestionEnum.FAIL;
            } else {
                // 5. 如果记录不存在，插入新记录
                UserQuestionRecord newRecord = new UserQuestionRecord();
                newRecord.setUserId(userId);
                newRecord.setQuestionId(realQuestionId);
                newRecord.setUserAnswer(answer);
                newRecord.setIsSubmitted(0); // 默认未提交
                newRecord.setCreateTime(LocalDateTime.now());
                newRecord.setUpdateTime(LocalDateTime.now());
                
                int insertResult = answerUserQuestionRecordMapper.insert(newRecord);
                
                return insertResult > 0 ? PostAnswerQuestionEnum.SUCCESS : PostAnswerQuestionEnum.FAIL;
            }
            
        } catch (Exception e) {
            return PostAnswerQuestionEnum.FAIL;
        }
    }

    @Override
    public PostAnswerQuestionEnum submitAnswer(Long answerId, Long userId) {
        try {
            // 参数校验
            if (answerId == null || userId == null || answerId <= 0 || userId <= 0) {
                return PostAnswerQuestionEnum.FAIL;
            }

            // 1. 根据回答ID查询记录
            UserQuestionRecord record = answerUserQuestionRecordMapper.selectById(answerId);
            if (record == null) {
                return PostAnswerQuestionEnum.ANSWER_NOT_FOUND;
            }

            // 2. 校验是否是本人的回答
            if (!record.getUserId().equals(userId)) {
                return PostAnswerQuestionEnum.UNAUTHORIZED;
            }

            // 3. 检查是否已经提交
            if (record.getIsSubmitted() != null && record.getIsSubmitted() == 1) {
                return PostAnswerQuestionEnum.ALREADY_SUBMITTED;
            }

            // 4. 更新提交状态
            record.setIsSubmitted(1);
            record.setUpdateTime(LocalDateTime.now());
            int updateResult = answerUserQuestionRecordMapper.updateById(record);

            // 5. 异步增加用户当日提交次数
            if (updateResult > 0) {
                incrementDailySubmitCount(userId);
            }

            return updateResult > 0 ? PostAnswerQuestionEnum.SUCCESS : PostAnswerQuestionEnum.FAIL;

        } catch (Exception e) {
            return PostAnswerQuestionEnum.FAIL;
        }
    }

    /**
     * 异步增加用户当日提交次数
     * @param userId 用户ID
     */
    @Async
    private void incrementDailySubmitCount(Long userId) {
        try {
            // 生成当日的Redis键
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String redisKey = AnswerAiKey.DAILY_SUBMIT_COUNT_KEY + userId + ":" + today;
            
            // 使用Redis的INCR操作增加计数
            redisTemplate.opsForValue().increment(redisKey, 1L);
            
            // 设置过期时间为3天，防止长期积压
            redisTemplate.expire(redisKey, 3, TimeUnit.DAYS);
            
        } catch (Exception e) {
            // 记录日志但不影响主流程
            // 可以考虑添加日志记录
        }
    }
}
