/**
 * File Name: PutUserAccountService.java
 * Description: 更新用户账户信息的服务接口
 * Author: holic512
 * Created Date: 2025-05-20
 * Version: 1.0
 * Usage:
 * 提供用户账户信息更新的服务接口，包括修改用户名和邮箱
 */
package org.example.serviceuser.user.account.service;

import java.util.Map;

/**
 * 更新用户账户信息的服务接口
 */
public interface PutUserAccountService {

    /**
     * 查询用户名是否存在
     *
     * @param username 用户名
     * @return 存在返回true, 不存在返回false
     */
    Boolean selectUsernameExists(String username);
    
    /**
     * 获取用户名剩余修改次数
     *
     * @param userId 用户ID
     * @return 剩余修改次数
     */
    int getRemainingUsernameChanges(Long userId);
    
    /**
     * 更新用户名
     *
     * @param userId   用户ID
     * @param username 新用户名
     * @return 更新结果，true为成功，false为失败
     */
    boolean updateUsername(Long userId, String username);

    
    /**
     * 初始化用户密码
     *
     * @param userId   用户ID
     * @param password 新密码
     * @return 初始化结果，true为成功，false为失败或密码已设置
     */
    boolean initializePassword(Long userId, String password);
    
    /**
     * 通过验证旧密码修改用户密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 更新结果
     * 1: 密码修改成功
     * 0: 旧密码错误
     * -1: 用户不存在
     */
    int updatePasswordWithOldPassword(Long userId, String oldPassword, String newPassword);

    /**
     * 发送密码重置验证码
     * 
     * @param userId 用户ID
     * @return 包含键值和脱敏邮箱的Map，失败则返回null
     */
    Map<String, String> sendPasswordResetCode(Long userId);
    
    /**
     * 验证验证码并重置密码
     *
     * @param userId      用户ID
     * @param key         唯一标识符
     * @param code        验证码
     * @param newPassword 新密码
     * @return 重置结果
     * 1: 密码重置成功
     * 0: 验证码错误或已过期
     * -1: 验证码与邮箱不匹配
     * -2: 用户不存在
     */
    int verifyCodeAndResetPassword(Long userId, String key, String code, String newPassword);


    /**
     * 发送邮箱更改验证码
     * 向指定邮箱发送验证码，用于验证更改邮箱绑定
     *
     * @param email 目标邮箱地址
     * @return 包含生成的唯一标识符(uid)的Map，失败则返回null
     */
    Map<String, String> sendEmailChangeCode(String email);

    /**
     * 向用户绑定的原邮箱发送验证码
     * 向用户当前绑定的邮箱发送验证码，用于验证原邮箱所有权
     *
     * @param userId 用户ID
     * @return 包含原邮箱(脱敏)和唯一标识符的Map，失败则返回null
     */
    Map<String, String> sendVerificationToOriginalEmail(Long userId);

    /**
     * 通过密码验证更新邮箱
     *
     * @param userId   用户ID
     * @param uid      唯一标识符
     * @param email    新邮箱地址
     * @param code     验证码
     * @param password 用户密码
     * @return 更新结果
     * 1: 更新成功
     * 0: 密码错误
     * -1: 验证码无效或已过期
     * -2: 邮箱不匹配
     * -3: 用户不存在
     */
    int updateEmailWithPassword(Long userId, String uid, String email, String code, String password);

    /**
     * 双重验证更新邮箱
     * 需要同时验证原邮箱和新邮箱的所有权
     *
     * @param userId          用户ID
     * @param originalUid     原邮箱的唯一标识符
     * @param originalCode    原邮箱的验证码
     * @param newEmail        新邮箱地址
     * @param newUid          新邮箱的唯一标识符
     * @param newCode         新邮箱的验证码
     * @return 更新结果
     * 1: 更新成功
     * 0: 原邮箱验证码错误
     * -1: 新邮箱验证码错误
     * -2: 原邮箱或新邮箱信息不匹配
     * -3: 用户不存在
     * -4: 新邮箱已被使用
     */
    int verifyAndUpdateEmailWithTwoSteps(Long userId, String originalUid, String originalCode, 
                                        String newEmail, String newUid, String newCode);

    /**
     * 解绑GitHub账号
     *
     * @param userId 用户ID
     * @return 解绑结果，true为成功，false为失败或未绑定
     */
    boolean unbindGithub(Long userId);
    
    /**
     * 检查用户是否已绑定GitHub账号
     *
     * @param userId 用户ID
     * @return 是否已绑定，true表示已绑定，false表示未绑定
     */
    boolean isGithubBindExists(Long userId);
}