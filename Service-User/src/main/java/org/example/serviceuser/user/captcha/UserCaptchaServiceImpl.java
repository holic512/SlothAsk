/**
 * File Name: UserCaptchaServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.captcha;

import org.example.serviceuser.config.Redis.RedisConfig;
import org.example.serviceuser.util.SliderCaptchaGenerator.SliderCaptchaGenerator;
import org.example.serviceuser.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserCaptchaServiceImpl implements UserCaptchaService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserCaptchaServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    private static final String CAPTCHA_PREFIX = RedisConfig.getKey() + "captcha:";

    @Override
    public Map<String, Object> captcha() throws IOException {
        SliderCaptchaGenerator.SliderCaptchaResult captchaResult = SliderCaptchaGenerator.generateCaptcha();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sliderImageBase64", captchaResult.getSliderImageBase64());
        resultMap.put("sliderImageType", "png"); // 滑块图片是PNG格式
        resultMap.put("backgroundImageBase64", captchaResult.getBackgroundImageBase64());
        resultMap.put("backgroundImageType", "jpeg"); // 背景图片是JPEG格式
        resultMap.put("yPosition", captchaResult.getYPosition());

        // 结果需存入 Redis，并设置过期时间。必须包含验证码相关信息，并携带 uid 及 x 参数，以便激活该 uid 的可用状态。
        // 需要验证码到时，需要提供 可用状态的 uid 参数。

        // 将结果存入 5 分钟
        String captchaUid = UuidUtil.generateUuid();
        redisTemplate.opsForValue().set(
                CAPTCHA_PREFIX + captchaUid,
                captchaResult.getXPosition(), // 获取运算结果
                5,
                TimeUnit.MINUTES
        );

        resultMap.put("captchaUid", CAPTCHA_PREFIX + captchaUid);

        return resultMap;
    }

    @Override
    public boolean validate(ValidateDto validateDto) {
        // 获取数据
        String validateUid = validateDto.getValidateUid();
        int validateX = validateDto.getValidateX();

        // 查询真实的 x
        Integer realValidateX = (Integer) redisTemplate.opsForValue().get(validateUid);
        if (realValidateX == null) {
            return false;
        }

        // 允许的误差范围
        int tolerance = 5;

        // 判断验证值是否在误差范围内
        return Math.abs(realValidateX - validateX) <= tolerance;
    }
}
