package org.example.serviceadmin.captcha.service;

import org.example.serviceadmin.auth.dto.CaptchaResponse;

/**
 * 验证码服务接口
 */
public interface CaptchaService {
    /**
     * 生成新的验证码
     * @param oldCaptchaId 旧的验证码ID（可为null）
     * @return 验证码响应对象
     */
    CaptchaResponse generateCaptcha(String oldCaptchaId);
} 