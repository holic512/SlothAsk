/**
 * File Name: GitHubAuthService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.sign.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.serviceuser.user.sign.dto.GitHubUserDTO;
import org.example.serviceuser.user.sign.enums.PostUserSignEnum;
import org.springframework.data.util.Pair;

public interface GitHubAuthService {
    String getAccessToken(String code,String state) throws JsonProcessingException;

    GitHubUserDTO getUserInfo(String accessToken) throws JsonProcessingException;
    
    /**
     * 处理GitHub用户登录
     * 如果GitHub用户未绑定系统用户，则自动创建新用户
     * 如果已绑定，则直接登录该用户
     * 
     * @param githubUser GitHub用户信息
     * @return 处理结果和登录令牌
     */
    Pair<PostUserSignEnum, Object> handleGitHubLogin(GitHubUserDTO githubUser);
}
