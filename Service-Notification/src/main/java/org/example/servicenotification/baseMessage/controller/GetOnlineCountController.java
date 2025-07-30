/**
 * File Name: GetOnlineCountController.java
 * Description: 在线人数统计控制器
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * 提供获取系统全局在线人数的接口，汇总各节点的在线连接数，用于统计分布式SSE连接总量。
 */

package org.example.servicenotification.baseMessage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.servicecommon.redisKey.BaseMessageKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("baseMessage")
public class GetOnlineCountController {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取全局在线人数接口
     * GET /sse/online-count
     * 
     * @return ApiResponse 包含在线人数统计结果
     */
    @GetMapping("/sse/online-count")
    public ApiResponse getOnlineCount() {
        try {
            // 构建在线用户键的匹配模式
            String pattern = BaseMessageKey.ONLINE_USER_KEY_PREFIX + "*";
            
            // 使用keys命令获取所有匹配的在线用户键
            Set<String> onlineUserKeys = redisTemplate.keys(pattern);
            
            // 统计在线人数
            int onlineCount = onlineUserKeys != null ? onlineUserKeys.size() : 0;
            
            log.info("当前在线人数统计: {}", onlineCount);
            
            return ApiResponse.ok("获取在线人数成功", onlineCount);
            
        } catch (Exception e) {
            log.error("获取在线人数失败", e);
            return ApiResponse.error(500, "获取在线人数失败: " + e.getMessage());
        }
    }

    /**
     * 获取展示用的在线人数接口（美化版）
     * GET /sse/display-online-count
     * 基于当前时间的虚拟在线人数算法，根据小时数 + 随机波动模拟真实在线人数
     * 
     * @return ApiResponse 包含美化后的在线人数
     */
    @GetMapping("/sse/display-online-count")
    public ApiResponse getDisplayOnlineCount() {
        try {
            // 获取真实在线人数作为基础
            String pattern = BaseMessageKey.ONLINE_USER_KEY_PREFIX + "*";
            Set<String> onlineUserKeys = redisTemplate.keys(pattern);
            int realOnlineCount = onlineUserKeys != null ? onlineUserKeys.size() : 0;
            
            // 基于时间的虚拟在线人数算法
            int displayOnlineCount = calculateDisplayOnlineCount(realOnlineCount);
            
            log.info("真实在线人数: {}, 展示在线人数: {}", realOnlineCount, displayOnlineCount);
            
            return ApiResponse.ok("获取展示在线人数成功", displayOnlineCount);
            
        } catch (Exception e) {
            log.error("获取展示在线人数失败", e);
            return ApiResponse.error(500, "获取展示在线人数失败: " + e.getMessage());
        }
    }
    
    /**
     * 计算展示用的在线人数
     * 核心算法：基于当前小时数 + 随机波动 + 真实在线人数基础
     * 
     * @param realCount 真实在线人数
     * @return 美化后的在线人数
     */
    private int calculateDisplayOnlineCount(int realCount) {
        // 获取当前小时（0-23）
        int currentHour = java.time.LocalTime.now().getHour();
        
        // 基础在线人数：根据时间段设置不同的基础值
        int baseCount;
        if (currentHour >= 9 && currentHour <= 12) {
            // 上午高峰期
            baseCount = 150 + (currentHour - 9) * 20;
        } else if (currentHour >= 14 && currentHour <= 17) {
            // 下午高峰期
            baseCount = 180 + (currentHour - 14) * 15;
        } else if (currentHour >= 19 && currentHour <= 22) {
            // 晚上高峰期
            baseCount = 200 + (currentHour - 19) * 25;
        } else if (currentHour >= 0 && currentHour <= 6) {
            // 深夜低谷期
            baseCount = 50 + currentHour * 5;
        } else {
            // 其他时间段
            baseCount = 100 + currentHour * 3;
        }
        
        // 添加随机波动（±20%）
        double randomFactor = 0.8 + (Math.random() * 0.4); // 0.8 到 1.2 之间
        int fluctuatedCount = (int) (baseCount * randomFactor);
        
        // 确保展示人数不少于真实人数，并添加适当的增幅
        int finalCount = Math.max(fluctuatedCount, realCount + 50);
        
        // 添加基于分钟的微调（让数据看起来更动态）
        int currentMinute = java.time.LocalTime.now().getMinute();
        int minuteAdjustment = (int) (Math.sin(currentMinute * Math.PI / 30) * 10);
        
        return finalCount + minuteAdjustment;
    }
}
