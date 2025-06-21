/**
 * File Name: GetUserSignInServiceImpl.java
 * Description: 用户签到查询服务实现类
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 * 实现用户签到状态查询相关的业务逻辑
 */
package org.example.serviceuser.user.signin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.servicecommon.entity.UserSignIn;
import org.example.servicecommon.redisKey.SignInKey;
import org.example.serviceuser.user.account.mapper.UserAccountMapper;
import org.example.serviceuser.user.signin.mapper.USignInPointsRecordMapper;
import org.example.serviceuser.user.signin.mapper.USignInUserPointsMapper;
import org.example.serviceuser.user.signin.mapper.USignInUserSignInMapper;
import org.example.serviceuser.user.signin.service.GetUserSignInService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GetUserSignInServiceImpl implements GetUserSignInService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final USignInUserSignInMapper userSignInMapper;
    private final USignInUserPointsMapper userPointsMapper;
    private final USignInPointsRecordMapper pointsRecordMapper;
    private final UserAccountMapper userAccountMapper;

    @Override
    public boolean checkTodaySignInStatus(Long userId) {
        try {
            // 1. 生成今日签到Redis键
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String signInKey = SignInKey.SIGN_IN_STATUS_KEY_PREFIX + today + ":" + userId;

            // 2. 检查Redis中是否已签到
            Boolean hasSignedInRedis = redisTemplate.hasKey(signInKey);
            if (Boolean.TRUE.equals(hasSignedInRedis)) {
                return true; // 今日已签到
            }

            // 3. 检查数据库中是否已签到
            QueryWrapper<UserSignIn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("sign_date", LocalDate.now());
            UserSignIn existingSignIn = userSignInMapper.selectOne(queryWrapper);
            
            if (existingSignIn != null) {
                // 数据库中已有签到记录，说明Redis缓存过期，重新设置Redis标记
                setRedisSignInFlag(signInKey);
                return true; // 今日已签到
            }

            return false; // 今日未签到
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时，为了安全起见，返回false（未签到）
            return false;
        }
    }

    /**
     * 设置Redis签到标记，过期时间为第二天零点
     * 
     * @param signInKey Redis键
     */
    private void setRedisSignInFlag(String signInKey) {
        // 计算到第二天零点的秒数
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowMidnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
        long secondsUntilMidnight = java.time.Duration.between(now, tomorrowMidnight).getSeconds();
        
        // 设置Redis标记，值为"1"，过期时间为第二天零点
        redisTemplate.opsForValue().set(signInKey, "1", secondsUntilMidnight, TimeUnit.SECONDS);
    }
}
