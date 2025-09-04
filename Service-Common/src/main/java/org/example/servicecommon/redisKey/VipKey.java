/**
 * File Name: VipKey.java
 * Description: VIP服务相关的 Redis 键定义
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * 定义用于VIP信息缓存、VIP状态检查等功能的 Redis 缓存键前缀，便于统一管理
 */
package org.example.servicecommon.redisKey;

public class VipKey {

    /**
     * Redis 中用于缓存用户VIP信息的键前缀
     * 格式为：{前缀}vip:user:{userId} -> UserVip对象JSON
     * 说明：
     * - 缓存用户的VIP详细信息，包括VIP类型、开始时间、结束时间等；
     * - 建议设置合理的过期时间（如30分钟），减少数据库查询压力；
     * - 当VIP信息发生变更时需要主动删除或更新缓存。
     */
    public static final String USER_VIP_INFO_KEY = CommonKey.KEY + "vip:user:";

    /**
     * Redis 中用于缓存用户VIP状态的键前缀
     * 格式为：{前缀}vip:status:{userId} -> boolean（true/false）
     * 说明：
     * - 缓存用户VIP是否生效的状态，用于快速判断；
     * - 建议设置较短的过期时间（如10分钟），确保状态的实时性；
     * - 适用于高频的VIP状态检查场景。
     */
    public static final String USER_VIP_STATUS_KEY = CommonKey.KEY + "vip:status:";

    /**
     * Redis 中用于记录VIP用户每日操作次数的缓存键前缀
     * 格式为：{前缀}vip:daily:count:{userId}:{yyyyMMdd} -> 整数（操作次数）
     * 示例：app:vip:daily:count:12345:20250115 -> 10
     * 说明：
     * - 记录VIP用户每日的特权操作次数，如AI问答次数等；
     * - 可设置键过期时间为2-3天，防止长期积压；
     * - 用于VIP用户的使用统计和限制控制。
     */
    public static final String VIP_DAILY_COUNT_KEY = CommonKey.KEY + "vip:daily:count:";

    /**
     * Redis 中用于标记VIP信息正在更新的锁键前缀
     * 格式为：{前缀}vip:lock:update:{userId} -> 1
     * 说明：
     * - 防止VIP信息并发更新导致的数据不一致；
     * - 建议设置较短的过期时间（如30秒），防止死锁；
     * - 在更新VIP信息前先获取锁，更新完成后释放锁。
     */
    public static final String VIP_UPDATE_LOCK_KEY = CommonKey.KEY + "vip:lock:update:";
}