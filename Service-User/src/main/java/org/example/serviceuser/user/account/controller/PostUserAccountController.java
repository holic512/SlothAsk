/**
 * File Name: PostUserAccountController.java
 * Description: 创建用户账户信息的控制器
 * Author: holic512
 * Created Date: 2025-05-22
 * Version: 1.0
 * Usage:
 * 提供用户账户信息创建的API接口，包括获取GitHub绑定验证码和验证GitHub绑定
 */
package org.example.serviceuser.user.account.controller;

import jakarta.validation.Valid;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.config.Redis.RedisConstants;
import org.example.serviceuser.user.account.request.VerifyGithubBindRequest;
import org.example.serviceuser.user.account.service.PostUserAccountService;
import org.example.serviceuser.user.account.service.PutUserAccountService;
import org.example.serviceuser.user.sign.dto.GitHubUserDTO;
import org.example.serviceuser.user.sign.service.GitHubAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user/account")
public class PostUserAccountController {

    private final PostUserAccountService postUserAccountService;
    private final PutUserAccountService putUserAccountService;
    private final GitHubAuthService gitHubAuthService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PostUserAccountController(PostUserAccountService postUserAccountService,
                                     PutUserAccountService putUserAccountService,
                                     GitHubAuthService gitHubAuthService,
                                     RedisTemplate<String, Object> redisTemplate) {
        this.postUserAccountService = postUserAccountService;
        this.putUserAccountService = putUserAccountService;
        this.gitHubAuthService = gitHubAuthService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取GitHub绑定的邮箱验证码
     * 根据对话ID查询用户邮箱，发送验证码邮件
     *
     * @param userId 从请求头中获取的用户ID
     * @return 发送结果响应，包含UID和脱敏邮箱
     */
    @PostMapping("sendGithubBindCode")
    public ApiResponse sendGithubBindCode(
            @RequestHeader(value = "X-User-Id") Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }
        
        // 检查用户是否已绑定GitHub账号
        boolean githubBound = putUserAccountService.isGithubBindExists(userId);
        if (githubBound) {
            return new ApiResponse(400, "您已绑定GitHub账号，无需重复绑定", false);
        }

        // 调用服务发送验证码
        Map<String, String> result = postUserAccountService.sendGithubBindCode(userId);

        // 返回发送结果
        if (result != null) {
            return new ApiResponse(200, "验证码发送成功", result);
        } else {
            return new ApiResponse(400, "验证码发送失败，请确认账号存在且已绑定邮箱", false);
        }
    }

    /**
     * 验证GitHub绑定的验证码
     * 验证成功后在Redis中标记用户正在进行GitHub绑定
     *
     * @param userId 从请求头中获取的用户ID
     * @param request 包含UID和验证码的请求对象
     * @return 验证结果响应
     */
    @PostMapping("verifyGithubBindCode")
    public ApiResponse verifyGithubBindCode(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid VerifyGithubBindRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }
        
        // 检查用户是否已绑定GitHub账号
        boolean githubBound = putUserAccountService.isGithubBindExists(userId);
        if (githubBound) {
            return new ApiResponse(400, "您已绑定GitHub账号，无需重复绑定", false);
        }

        // 调用服务验证验证码
        int result = postUserAccountService.verifyGithubBindCode(userId, request.getUid(), request.getCode());

        // 返回验证结果
        return switch (result) {
            case 1 -> new ApiResponse(200, "验证成功，请继续完成GitHub授权", true);
            case 0 -> new ApiResponse(400, "验证码错误或已过期", false);
            case -1 -> new ApiResponse(400, "验证码与邮箱不匹配", false);
            case -2 -> new ApiResponse(400, "用户不存在", false);
            case -3 -> new ApiResponse(400, "用户已绑定GitHub账号", false);
            default -> new ApiResponse(500, "系统错误", false);
        };
    }
    
    /**
     * 绑定GitHub账号
     * 处理GitHub授权后的回调，并将GitHub账号绑定到当前用户
     *
     * @param userId 从请求头中获取的用户ID
     * @param code GitHub授权码
     * @param state 状态参数，用于防止CSRF攻击
     * @return 绑定结果响应
     */
    @PostMapping("bindGithub")
    public ApiResponse bindGithub(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam("code") String code,
            @RequestParam("state") String state) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }
        
        // 检查用户是否已绑定GitHub账号
        boolean githubBound = putUserAccountService.isGithubBindExists(userId);
        if (githubBound) {
            return new ApiResponse(400, "您已绑定GitHub账号，无需重复绑定", false);
        }
        
        // 检查用户是否已完成邮箱验证步骤
        String editingKey = RedisConstants.Email.GITHUB_BIND_EDITING_PREFIX + userId;
        Boolean isEditing = (Boolean) redisTemplate.opsForValue().get(editingKey);
        if (isEditing == null || !isEditing) {
            return new ApiResponse(400, "请先完成邮箱验证再绑定GitHub账号", false);
        }
        
        try {
            // 1. 使用授权码获取访问令牌
            String accessToken = gitHubAuthService.getAccessToken(code, state);
            
            // 2. 使用访问令牌获取GitHub用户信息
            GitHubUserDTO githubUser = gitHubAuthService.getUserInfo(accessToken);
            
            // 3. 绑定GitHub账号到当前用户
            boolean bindResult = postUserAccountService.bindGithubAccount(userId, githubUser);
            
            // 4. 删除Redis中的编辑态标记
            redisTemplate.delete(editingKey);
            
            // 5. 返回绑定结果
            if (bindResult) {
                return new ApiResponse(200, "GitHub账号绑定成功", true);
            } else {
                return new ApiResponse(400, "GitHub账号绑定失败", false);
            }
        } catch (Exception e) {
            return new ApiResponse(500, "绑定过程中发生错误: " + e.getMessage(), false);
        }
    }
}
