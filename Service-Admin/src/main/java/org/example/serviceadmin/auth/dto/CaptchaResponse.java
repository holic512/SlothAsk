package org.example.serviceadmin.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 验证码响应对象
 */
@Data
@AllArgsConstructor
public class CaptchaResponse {
    /**
     * 验证码ID
     */
    private String captchaId;
    
    /**
     * Base64编码的验证码图片
     */
    private String imageBase64;
} 