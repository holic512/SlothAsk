/**
 * File Name: GetUserAccountService.java
 * Description: 获取用户账户信息的服务接口
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供获取用户账户信息的方法
 */
package org.example.serviceuser.user.account.service;

import org.example.serviceuser.user.account.dto.UserAccountDTO;

public interface GetUserAccountService {

    /**
     * 获取用户名和邮箱
     *
     * @param userId 用户ID
     * @return 包含用户名和邮箱的DTO，如果用户不存在则返回null
     */
    UserAccountDTO getUsernameAndEmail(Long userId);
    
    /**
     * 检查用户密码是否为空（null或空字符串）
     *
     * @param userId 用户ID
     * @return true表示密码为空，false表示密码不为空，null表示用户不存在
     */
    Boolean isPasswordEmpty(Long userId);
    
    /**
     * 检查用户是否绑定了GitHub账号
     *
     * @param userId 用户ID
     * @return true表示已绑定，false表示未绑定，null表示用户不存在
     */
    Boolean isGithubBound(Long userId);
}