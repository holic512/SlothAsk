/**
 * File Name: GitHubLoginController.java
 * Description: GitHub OAuth登录控制器
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * 处理GitHub OAuth回调和用户登录
 */
package org.example.serviceuser.user.sign.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.user.sign.dto.GitHubUserDTO;
import org.example.serviceuser.user.sign.enums.PostUserSignEnum;
import org.example.serviceuser.user.sign.exception.GitHubAuthException;
import org.example.serviceuser.user.sign.service.GitHubAuthService;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/oauth/github")
@RequiredArgsConstructor
public class GitHubLoginController {

    private final GitHubAuthService gitHubAuthService;

    /**
     * GitHub OAuth回调处理
     * 处理GitHub授权后的回调，获取用户信息并完成登录
     *
     * @param code GitHub授权码
     * @param state 状态参数，用于防止CSRF攻击
     * @return 响应对象，包含登录结果
     */
    @GetMapping("/callback")
    public ApiResponse githubCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        try {
            // 1. 使用授权码获取访问令牌
            String accessToken = gitHubAuthService.getAccessToken(code, state);
            
            // 2. 使用访问令牌获取GitHub用户信息
            GitHubUserDTO user = gitHubAuthService.getUserInfo(accessToken);
            
            // 3. 处理GitHub用户登录
            Pair<PostUserSignEnum, Object> result = gitHubAuthService.handleGitHubLogin(user);
            
            // 4. 根据结果返回响应
            if (result.getFirst() == PostUserSignEnum.SUCCESS_LOGIN) {
                SaTokenInfo tokenInfo = (SaTokenInfo) result.getSecond();
                log.info("GitHub用户{}登录成功", user.getLogin());
                return new ApiResponse(200, "登录成功，GitHub 用户：" + user.getLogin(), tokenInfo);
            } else {
                log.warn("GitHub登录结果异常: {}", result.getFirst());
                return new ApiResponse(400, "登录失败：" + result.getFirst().getMessage());
            }
        } catch (GitHubAuthException e) {
            log.warn("GitHub登录失败: {}", e.getMessage());
            return new ApiResponse(400, "登录失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("GitHub登录发生异常: {}", e.getMessage(), e);
            return new ApiResponse(500, "登录过程中发生错误");
        }
    }
}