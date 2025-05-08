/**
 * File Name: GitHubUserDTO.java
 * Description: GitHub用户数据传输对象
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * 用于存储和传输从GitHub API获取的用户信息
 */
package org.example.serviceuser.user.sign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GitHub用户数据传输对象
 * 对应GitHub API返回的用户信息字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubUserDTO {
    
    /**
     * GitHub用户ID
     */
    private Long id;
    
    /**
     * GitHub登录名
     */
    private String login;
    
    /**
     * GitHub用户名
     */
    private String name;
    
    /**
     * 用户头像URL
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 用户简介
     */
    private String bio;
    
    /**
     * 公共仓库数量
     */
    @JsonProperty("public_repos")
    private Integer publicRepos;
    
    /**
     * 关注者数量
     */
    private Integer followers;
    
    /**
     * 关注的用户数量
     */
    private Integer following;
    
    /**
     * 用户主页URL
     */
    @JsonProperty("html_url")
    private String htmlUrl;
    
    /**
     * 用户创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;
    
    /**
     * 用户更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;
}

