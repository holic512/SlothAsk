/**
 * File Name: SignInKey.java
 * Description: 用户签到状态 Redis Key 定义类，用于标记每日签到状态
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 *  - 用于缓存用户是否已签到（判断是否存在该 Key）
 *  - Key 格式为：{前缀}sign:in:{yyyyMMdd}:{userId}
 *  - 存在即表示已签到，不存在即未签到
 */
package org.example.servicecommon.redisKey;

public class SignInKey {

    /**
     * 用户每日签到状态键前缀
     * Redis Key 格式：{前缀}sign:in:{yyyyMMdd}:{userId}
     * 示例：app:sign:in:20250620:12345 -> "1"
     * TTL：设为第二天零点，自动过期
     */
    public static final String SIGN_IN_STATUS_KEY_PREFIX = CommonKey.KEY + "sign:in:";

}
