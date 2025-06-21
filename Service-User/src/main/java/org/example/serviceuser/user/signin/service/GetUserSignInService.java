/**
 * File Name: GetUserSignInService.java
 * Description: 用户签到查询服务接口
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 * 提供用户签到状态查询相关的业务逻辑接口
 */
package org.example.serviceuser.user.signin.service;

public interface GetUserSignInService {
    
    /**
     * 检查用户今日签到状态
     * 
     * @param userId 用户ID
     * @return 签到状态
     *         true: 今日已签到
     *         false: 今日未签到
     */
    boolean checkTodaySignInStatus(Long userId);
}
