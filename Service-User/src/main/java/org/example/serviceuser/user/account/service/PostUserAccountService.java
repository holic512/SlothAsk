/**
 * File Name: PostUserAccountService.java
 * Description: 创建用户账户信息的服务接口
 * Author: holic512
 * Created Date: 2025-05-22
 * Version: 1.0
 * Usage:
 * 提供用户账户信息创建的服务接口，包括获取GitHub绑定验证码和验证GitHub绑定码
 */
package org.example.serviceuser.user.account.service;

import org.example.serviceuser.user.sign.dto.GitHubUserDTO;

import java.util.Map;

/**
 * 创建用户账户信息的服务接口
 */
public interface PostUserAccountService {
    
    /**
     * 发送GitHub绑定验证码
     * 根据用户ID查询邮箱并发送验证码邮件
     * 
     * @param userId 用户ID
     * @return 包含唯一标识符和脱敏邮箱的Map，失败则返回null
     */
    Map<String, String> sendGithubBindCode(Long userId);
    
    /**
     * 验证GitHub绑定验证码
     * 验证成功后在Redis中标记用户正在进行GitHub绑定
     * 
     * @param userId 用户ID
     * @param uid 唯一标识符
     * @param code 验证码
     * @return 验证结果
     * 1: 验证成功
     * 0: 验证码错误或已过期
     * -1: 验证码与邮箱不匹配
     * -2: 用户不存在
     * -3: 用户已绑定GitHub账号
     */
    int verifyGithubBindCode(Long userId, String uid, String code);
    
    /**
     * 绑定GitHub账号到用户
     * 如果该GitHub账号已绑定其他用户，删除原有绑定并重新绑定
     * 
     * @param userId 用户ID
     * @param githubUser GitHub用户信息
     * @return 绑定结果，true表示成功，false表示失败
     */
    boolean bindGithubAccount(Long userId, GitHubUserDTO githubUser);
} 