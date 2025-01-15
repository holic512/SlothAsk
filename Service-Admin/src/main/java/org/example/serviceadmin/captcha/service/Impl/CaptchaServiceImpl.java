package org.example.serviceadmin.captcha.service.Impl;

import com.pig4cloud.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.example.serviceadmin.auth.dto.CaptchaResponse;
import org.example.serviceadmin.captcha.service.CaptchaService;
import org.example.serviceadmin.config.Redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现类
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CAPTCHA_PREFIX = RedisConfig.getKey() + "login:captcha:";
    private static final long CAPTCHA_EXPIRE_TIME = 60; // 验证码过期时间（秒）

    @Autowired
    public CaptchaServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CaptchaResponse generateCaptcha(String oldCaptchaId) {
        // 如果存在旧的验证码ID，先删除
        if (oldCaptchaId != null && !oldCaptchaId.isEmpty()) {
            redisTemplate.delete(CAPTCHA_PREFIX + oldCaptchaId);
        }

        // 创建算术验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);  // 两位数运算

        // 生成新的验证码ID
        String captchaId = UUID.randomUUID().toString();
        
        // 保存验证码结果到Redis
        redisTemplate.opsForValue().set(
            CAPTCHA_PREFIX + captchaId,
            captcha.text(),
            CAPTCHA_EXPIRE_TIME,
            TimeUnit.SECONDS
        );

        return new CaptchaResponse(captchaId, captcha.toBase64());
    }
} 