/**
 * File Name: PostUserSignInService.java
 * Description: 用户签到服务接口
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 * 提供用户签到相关的业务逻辑接口
 */
package org.example.serviceuser.user.signin.service;

public interface PostUserSignInService {
    
    /**
     * 用户签到
     * 
     * @param userId 用户ID
     * @param clientIp 客户端IP地址
     * @return 签到结果
     *         1: 签到成功
     *         0: 今日已签到
     *         -1: 用户不存在
     *         -2: 系统错误
     */
    int signIn(Long userId, String clientIp);
}
