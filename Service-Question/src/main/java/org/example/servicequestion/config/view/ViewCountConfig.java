package org.example.servicequestion.config.view;

import org.example.servicequestion.config.Redis.RedisConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 访问量配置类
 */
@Configuration
public class ViewCountConfig {
    
    /**
     * Redis中存储访问记录的键前缀
     */
    public static final String VIEW_COUNT_INCR_KEY = RedisConfig.getKey() + "Question:ViewCount:Incr:";
    
    /**
     * Redis中存储用户访问记录的键前缀，用于防抖
     */
    public static final String USER_VIEW_RECORD_KEY = RedisConfig.getKey() + "Question:View:User:";
    
    /**
     * 访问防抖时间间隔（秒）
     * 同一用户在DEBOUNCE_INTERVAL秒内多次访问同一题目只记录一次访问量
     */
    public static final int DEBOUNCE_INTERVAL = 300; // 5分钟
    
    /**
     * 定时任务执行间隔（毫秒）
     */
    public static final long SYNC_INTERVAL = 5 * 60 * 1000; // 5分钟
} 