/**
 * File Name: PostUserSignInController.java
 * Description: 用户签到控制器
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 * 提供用户签到相关的API接口
 */
package org.example.serviceuser.user.signin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.signin.service.PostUserSignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/signin")
public class PostUserSignInController {

    private final PostUserSignInService postUserSignInService;

    @Autowired
    public PostUserSignInController(PostUserSignInService postUserSignInService) {
        this.postUserSignInService = postUserSignInService;
    }

    /**
     * 用户签到接口
     * 
     * @param userId 从请求头中获取的用户ID
     * @param request HTTP请求对象，用于获取客户端IP
     * @return 签到结果响应
     */
    @PostMapping("signIn")
    public ApiResponse signIn(
            @RequestHeader(value = "X-User-Id") Long userId,
            HttpServletRequest request) {
        
        // 参数校验
        if (userId == null) {
            return ApiResponse.error(400, "用户ID不能为空");
        }
        
        // 获取客户端IP地址
        String clientIp = getClientIp(request);
        
        // 调用服务执行签到
        int result = postUserSignInService.signIn(userId, clientIp);
        
        // 返回签到结果
        return switch (result) {
            case 1 -> ApiResponse.ok("签到成功，获得10积分！", true);
            case 0 -> ApiResponse.ok("今日已签到", false);
            case -1 -> ApiResponse.error(400, "用户不存在");
            case -2 -> ApiResponse.error(500, "系统错误，请稍后重试");
            default -> ApiResponse.error(500, "未知错误");
        };
    }

    /**
     * 获取客户端真实IP地址
     * 
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 如果通过多级代理，X-Forwarded-For的值为：客户端IP,代理1IP,代理2IP,...
        // 取第一个非unknown的有效IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

}
