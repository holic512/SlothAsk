/**
 * File Name: controller.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.captcha;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("user/captcha")
public class UserCaptchaController {

    private UserCaptchaService userCaptchaService;

    @Autowired
    public void setUserCaptchaService(UserCaptchaService userCaptchaService) {
        this.userCaptchaService = userCaptchaService;
    }

    @GetMapping("captcha")
    public ApiResponse captcha() throws IOException {
        Map<String, Object> result = userCaptchaService.captcha();
        return new ApiResponse(200, "获取验证码成功", result);
    }

    @PostMapping("validate")
    public ApiResponse validate(@RequestBody ValidateDto validateDto) {
        if (userCaptchaService.validate(validateDto))
            return new ApiResponse(200, "验证成功");
        else
            return new ApiResponse(400, "验证失败");
    }

}
