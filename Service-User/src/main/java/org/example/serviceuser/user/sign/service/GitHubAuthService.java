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
     * 新策略：
     * 1. 如果GitHub账号已经绑定系统用户，直接登录该用户
     * 2. 如果未绑定但GitHub用户的邮箱在系统中存在，则绑定到该邮箱账户
     * 3. 如果邮箱不存在，则创建新用户，并确保设置邮箱
     * 
     * @param githubUser GitHub用户信息
     * @param accessToken GitHub访问令牌，用于获取额外信息如邮箱
     * @return 处理结果和登录令牌
     */
    Pair<PostUserSignEnum, Object> handleGitHubLogin(GitHubUserDTO githubUser, String accessToken);
}
