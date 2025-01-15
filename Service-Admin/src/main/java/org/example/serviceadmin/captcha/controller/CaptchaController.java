package org.example.serviceadmin.captcha.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.serviceadmin.auth.dto.CaptchaResponse;
import org.example.serviceadmin.captcha.service.CaptchaService;
import org.example.serviceadmin.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码控制器
 */
@Slf4j
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    @Autowired
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * 获取验证码
     * @param oldCaptchaId 旧的验证码ID（可选）
     * @return API响应对象
     */
    @GetMapping("/get")
    public ApiResponse getCaptcha(@RequestParam(required = false) String oldCaptchaId) {
        try {
            CaptchaResponse captchaResponse = captchaService.generateCaptcha(oldCaptchaId);
            return new ApiResponse(200, "验证码生成成功", captchaResponse);
        } catch (Exception e) {
            log.error("Generate captcha failed", e);
            return new ApiResponse(500, "验证码生成失败");
        }
    }
} 