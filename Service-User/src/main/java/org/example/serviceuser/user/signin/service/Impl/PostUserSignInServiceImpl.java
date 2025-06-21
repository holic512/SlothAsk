/**
 * File Name: PostUserSignInServiceImpl.java
 * Description: 用户签到服务实现类
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 * 实现用户签到的业务逻辑，包括Redis缓存检查、数据库操作和积分奖励
 */
package org.example.serviceuser.user.signin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.servicecommon.entity.PointsRecord;
import org.example.servicecommon.entity.UserPoints;
import org.example.servicecommon.entity.UserSignIn;
import org.example.servicecommon.redisKey.SignInKey;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.user.account.mapper.UserAccountMapper;
import org.example.serviceuser.user.signin.mapper.USignInPointsRecordMapper;
import org.example.serviceuser.user.signin.mapper.USignInUserPointsMapper;
import org.example.serviceuser.user.signin.mapper.USignInUserSignInMapper;
import org.example.serviceuser.user.signin.service.PostUserSignInService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 用户签到服务实现类
 */
@Service
@RequiredArgsConstructor
public class PostUserSignInServiceImpl implements PostUserSignInService {

    // 签到奖励积分
    private static final int SIGN_IN_POINTS = 10;
    private final RedisTemplate<String, Object> redisTemplate;
    private final USignInUserSignInMapper userSignInMapper;
    private final USignInUserPointsMapper userPointsMapper;
    private final USignInPointsRecordMapper pointsRecordMapper;
    private final UserAccountMapper userAccountMapper;

    /**
     * 用户签到
     * 
     * @param userId 用户ID
     * @param clientIp 客户端IP地址
     * @return 签到结果
     *         1: 签到成功
     *         0: 今日已签到
     *         -1: 用户不存在
     *         -2: 系统错误
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int signIn(Long userId, String clientIp) {
        try {
            // 1. 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return -1; // 用户不存在
            }

            // 2. 生成今日签到Redis键
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String signInKey = SignInKey.SIGN_IN_STATUS_KEY_PREFIX + today + ":" + userId;

            // 3. 检查Redis中是否已签到
            Boolean hasSignedInRedis = redisTemplate.hasKey(signInKey);
            if (Boolean.TRUE.equals(hasSignedInRedis)) {
                return 0; // 今日已签到
            }

            // 4. 检查数据库中是否已签到
            QueryWrapper<UserSignIn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("sign_date", LocalDate.now());
            UserSignIn existingSignIn = userSignInMapper.selectOne(queryWrapper);
            
            if (existingSignIn != null) {
                // 数据库中已有签到记录，说明Redis缓存过期，重新设置Redis标记
                setRedisSignInFlag(signInKey);
                return 0; // 今日已签到
            }

            // 5. 执行签到逻辑
            LocalDateTime now = LocalDateTime.now();
            
            // 5.1 插入签到记录
            UserSignIn userSignIn = new UserSignIn();
            userSignIn.setUserId(userId);
            userSignIn.setSignDate(LocalDate.now());
            userSignIn.setSignTime(now);
            userSignIn.setSignIp(clientIp);
            userSignInMapper.insert(userSignIn);

            // 5.2 处理用户积分
            handleUserPoints(userId, now);

            // 5.3 设置Redis签到标记
            setRedisSignInFlag(signInKey);

            return 1; // 签到成功
        } catch (Exception e) {
            e.printStackTrace();
            return -2; // 系统错误
        }
    }

    /**
     * 处理用户积分逻辑
     * 
     * @param userId 用户ID
     * @param signTime 签到时间
     */
    private void handleUserPoints(Long userId, LocalDateTime signTime) {
        // 1. 查询用户积分记录
        QueryWrapper<UserPoints> pointsQueryWrapper = new QueryWrapper<>();
        pointsQueryWrapper.eq("user_id", userId);
        UserPoints userPoints = userPointsMapper.selectOne(pointsQueryWrapper);

        if (userPoints == null) {
            // 用户积分记录不存在，创建新记录
            userPoints = new UserPoints();
            userPoints.setUserId(userId);
            userPoints.setPoints(SIGN_IN_POINTS);
            userPoints.setTotalPoints(SIGN_IN_POINTS);
            userPoints.setUsedPoints(0);
            userPointsMapper.insert(userPoints);
        } else {
            // 更新现有积分记录
            userPoints.setPoints(userPoints.getPoints() + SIGN_IN_POINTS);
            userPoints.setTotalPoints(userPoints.getTotalPoints() + SIGN_IN_POINTS);
            userPointsMapper.updateById(userPoints);
        }

        // 2. 插入积分变动记录
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setUserId(userId);
        pointsRecord.setPoints(SIGN_IN_POINTS);
        pointsRecord.setType(1); // 1: 获得
        pointsRecord.setSource(1); // 1: 签到
        pointsRecord.setDescription("每日签到奖励");
        pointsRecordMapper.insert(pointsRecord);
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
