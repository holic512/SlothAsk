/**
 * File Name: SendAiAnalysisServiceImpl.java
 * Description: AI解析服务实现类，提供发送AI解析请求的功能
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 实现AI解析服务接口，包括权限验证、Redis状态检查、设置解析状态和发送RabbitMQ消息
 */
package org.example.servicequestion.user.aiAnalysis.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.servicecommon.entity.UserAnswerAiAnalysis;
import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.config.rabbit.AiAnalysisRabbitConfig;
import org.example.servicequestion.entity.UserQuestionRecord;
import org.example.servicequestion.user.aiAnalysis.dto.AiAnalysisMessage;
import org.example.servicequestion.user.aiAnalysis.enums.SendAiAnalysisEnum;
import org.example.servicequestion.user.aiAnalysis.mapper.AiAnalysisUAAiAnalysisMapper;
import org.example.servicequestion.user.aiAnalysis.mapper.AiAnalysisUQRecordMapper;
import org.example.servicequestion.user.aiAnalysis.service.SendAiAnalysisService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SendAiAnalysisServiceImpl implements SendAiAnalysisService {

    private final AiAnalysisUQRecordMapper aiAnalysisUQRecordMapper;
    private final AiAnalysisUAAiAnalysisMapper aiAnalysisUAAiAnalysisMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final AiAnalysisRabbitConfig aiAnalysisRabbitConfig;

    @Autowired
    public SendAiAnalysisServiceImpl(AiAnalysisUQRecordMapper aiAnalysisUQRecordMapper,
                                   AiAnalysisUAAiAnalysisMapper aiAnalysisUAAiAnalysisMapper,
                                   RedisTemplate<String, Object> redisTemplate,
                                   RabbitTemplate rabbitTemplate,
                                   AiAnalysisRabbitConfig aiAnalysisRabbitConfig) {
        this.aiAnalysisUQRecordMapper = aiAnalysisUQRecordMapper;
        this.aiAnalysisUAAiAnalysisMapper = aiAnalysisUAAiAnalysisMapper;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.aiAnalysisRabbitConfig = aiAnalysisRabbitConfig;
    }

    @Override
    public SendAiAnalysisEnum sendAiAnalysisRequest(Long answerId, Long userId) {
        try {
            // 参数校验
            if (answerId == null || userId == null || answerId <= 0 || userId <= 0) {
                return SendAiAnalysisEnum.FAIL;
            }

            // 1. 根据回答ID查询记录
            UserQuestionRecord record = aiAnalysisUQRecordMapper.selectById(answerId);
            if (record == null) {
                return SendAiAnalysisEnum.ANSWER_NOT_FOUND;
            }

            // 2. 校验是否是本人的回答
            if (!record.getUserId().equals(userId)) {
                return SendAiAnalysisEnum.UNAUTHORIZED;
            }

            // 3. 检查回答是否已提交
            if (record.getIsSubmitted() == null || record.getIsSubmitted() != 1) {
                return SendAiAnalysisEnum.NOT_SUBMITTED;
            }

            // 4. 检查数据库中是否已存在AI解析记录（排除逻辑删除的记录）
            QueryWrapper<UserAnswerAiAnalysis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("record_id", answerId)
                       .eq("is_deleted", 0); // 排除逻辑删除的记录
            
            Long existingCount = aiAnalysisUAAiAnalysisMapper.selectCount(queryWrapper);
            if (existingCount != null && existingCount > 0) {
                return SendAiAnalysisEnum.ALREADY_ANALYZED;
            }

            // 5. 检查是否正在进行AI解析
            String parsingKey = RedisKey.ANSWER_AI_PARSING_KEY + answerId;
            Boolean isExists = redisTemplate.hasKey(parsingKey);
            if (Boolean.TRUE.equals(isExists)) {
                return SendAiAnalysisEnum.ALREADY_PARSING;
            }

            // 6. 设置Redis解析状态锁（有效期10分钟）
            redisTemplate.opsForValue().set(parsingKey, "1", 10, TimeUnit.MINUTES);

            // 7. 发送RabbitMQ消息
            AiAnalysisMessage analysisMessage = new AiAnalysisMessage(answerId.toString());
            aiAnalysisRabbitConfig.sendMessage(rabbitTemplate, analysisMessage);

            return SendAiAnalysisEnum.SUCCESS;

        } catch (Exception e) {
            // 如果发生异常，清除Redis锁
            String parsingKey = RedisKey.ANSWER_AI_PARSING_KEY + answerId;
            redisTemplate.delete(parsingKey);
            return SendAiAnalysisEnum.FAIL;
        }
    }
}
