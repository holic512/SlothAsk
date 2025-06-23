/**
 * File Name: DailySubmitCountScheduler.java
 * Description: 用户每日提交次数定时任务
 * Author: holic512
 * Created Date: 2025-06-23
 * Version: 1.0
 * Usage:
 * 每天凌晨0点将Redis中的提交次数数据批量写入数据库
 */
package org.example.servicequestion.user.study.hotMap;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.entity.UserDailySubmitCount;
import org.example.servicecommon.redisKey.AnswerAiKey;
import org.example.servicequestion.user.study.mapper.UserDailySubmitCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * 用户每日提交次数定时任务
 * 每天凌晨0点将Redis中的提交次数数据批量写入数据库
 */
@Slf4j
@Component
public class DailySubmitCountScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDailySubmitCountMapper userDailySubmitCountMapper;

    @Autowired
    public DailySubmitCountScheduler(RedisTemplate<String, Object> redisTemplate,
                                   UserDailySubmitCountMapper userDailySubmitCountMapper) {
        this.redisTemplate = redisTemplate;
        this.userDailySubmitCountMapper = userDailySubmitCountMapper;
    }

    /**
     * 每天凌晨0点执行定时任务
     * 将前一天的Redis缓存数据批量写入数据库
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void syncDailySubmitCountToDatabase() {
        try {
            // 获取前一天的日期
            LocalDate yesterday = LocalDate.now().minusDays(1);
            String yesterdayStr = yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            
            log.info("开始同步{}的提交次数数据到数据库", yesterday);
            
            // 构建Redis键的模式，查找所有用户前一天的提交次数
            String keyPattern = AnswerAiKey.DAILY_SUBMIT_COUNT_KEY + "*:" + yesterdayStr;
            Set<String> keys = redisTemplate.keys(keyPattern);
            
            if (keys == null || keys.isEmpty()) {
                log.info("没有找到{}的提交次数数据", yesterday);
                return;
            }
            
            int successCount = 0;
            int errorCount = 0;
            
            // 遍历所有匹配的Redis键
            for (String redisKey : keys) {
                try {
                    // 从Redis键中提取用户ID
                    // 键格式: app:answerSubmit:count:{userId}:{yyyyMMdd}
                    String[] parts = redisKey.split(":");
                    if (parts.length < 4) {
                        log.warn("Redis键格式不正确: {}", redisKey);
                        continue;
                    }
                    
                    Long userId = Long.valueOf(parts[3]);
                    
                    // 获取Redis中的提交次数
                    Object redisValue = redisTemplate.opsForValue().get(redisKey);
                    if (redisValue == null) {
                        continue;
                    }
                    
                    Integer redisCount = Integer.valueOf(redisValue.toString());
                    
                    // 查询数据库中是否已存在该记录
                    QueryWrapper<UserDailySubmitCount> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id", userId)
                               .eq("submit_date", yesterday);
                    
                    UserDailySubmitCount existingRecord = userDailySubmitCountMapper.selectOne(queryWrapper);
                    
                    if (existingRecord == null) {
                        // 数据库中不存在记录，插入新记录
                        UserDailySubmitCount newRecord = new UserDailySubmitCount();
                        newRecord.setUserId(userId);
                        newRecord.setSubmitDate(yesterday);
                        newRecord.setSubmitCount(redisCount);
                        
                        userDailySubmitCountMapper.insert(newRecord);
                        log.debug("插入新记录: 用户ID={}, 日期={}, 次数={}", userId, yesterday, redisCount);
                    } else {
                        // 数据库中已存在记录，检查次数是否一致
                        if (!existingRecord.getSubmitCount().equals(redisCount)) {
                            // 次数不一致，更新数据库记录
                            existingRecord.setSubmitCount(redisCount);
                            userDailySubmitCountMapper.updateById(existingRecord);
                            log.debug("更新记录: 用户ID={}, 日期={}, 原次数={}, 新次数={}", 
                                    userId, yesterday, existingRecord.getSubmitCount(), redisCount);
                        }
                    }
                    
                    // 同步成功后删除Redis中的数据
                    redisTemplate.delete(redisKey);
                    successCount++;
                    
                } catch (Exception e) {
                    log.error("处理Redis键{}时发生错误: {}", redisKey, e.getMessage(), e);
                    errorCount++;
                }
            }
            
            log.info("{}的提交次数数据同步完成，成功: {}, 失败: {}", yesterday, successCount, errorCount);
            
        } catch (Exception e) {
            log.error("定时任务执行失败: {}", e.getMessage(), e);
        }
    }
}