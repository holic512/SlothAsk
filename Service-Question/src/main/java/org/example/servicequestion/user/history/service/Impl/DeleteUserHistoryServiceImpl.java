/**
 * File Name: DeleteUserHistoryServiceImpl.java
 * Description: 删除用户历史服务实现类
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.service.Impl;

import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.user.history.mapper.UserHistoryUserQuestionHistoryMapper;
import org.example.servicequestion.user.history.service.DeleteUserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserHistoryServiceImpl implements DeleteUserHistoryService {

    private final UserHistoryUserQuestionHistoryMapper userHistoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";


    @Autowired
    public DeleteUserHistoryServiceImpl(UserHistoryUserQuestionHistoryMapper userHistoryMapper, RedisTemplate<String, Object> redisTemplate) {
        this.userHistoryMapper = userHistoryMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean deleteHistoryRecord(Long userId, String questionVId) {
        if (userId == null || userId <= 0 || questionVId == null || questionVId.isEmpty()) {
            return false;
        }

        // 获取真实题目id
        String questionId = (String) redisTemplate.opsForValue().get(VidKey + questionVId);

        try {
            // 这里需要根据实际数据库表结构实现删除逻辑
            int rowsAffected = userHistoryMapper.deleteHistoryRecord(userId, questionId);
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean clearDayHistoryRecords(Long userId, String date) {
        if (userId == null || userId <= 0 || date == null || date.isEmpty()) {
            return false;
        }

        try {
            // 这里需要根据实际数据库表结构实现删除逻辑
            // 由于日期可能是"今天"、"昨天"、"前天"等特殊格式，可能需要转换为实际日期
            String actualDate = convertDisplayDateToActualDate(date);
            int rowsAffected = userHistoryMapper.clearDayHistoryRecords(userId, actualDate);
            return rowsAffected > 0;
        } catch (Exception e) {
            // 日志记录异常
            return false;
        }
    }

    /**
     * 将显示日期转换为实际日期
     *
     * @param displayDate 显示日期（如：今天、昨天、前天等）
     * @return 实际日期格式（如：2025-04-16）
     */
    private String convertDisplayDateToActualDate(String displayDate) {
        // 根据实际业务逻辑实现转换
        // 这里是简化实现，实际应用中可能需要更复杂的逻辑
        return switch (displayDate) {
            case "今天" -> java.time.LocalDate.now().toString();
            case "昨天" -> java.time.LocalDate.now().minusDays(1).toString();
            case "前天" -> java.time.LocalDate.now().minusDays(2).toString();
            default -> displayDate; // 假设其他日期已经是标准格式
        };
    }
} 