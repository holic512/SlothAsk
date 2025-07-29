/**
 * File Name: BaseMessageKey.java
 * Description: 消息系统相关的 Redis 键定义
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * 定义用于消息广播、在线状态标记、离线消息缓存等功能的 Redis 缓存键前缀，便于统一管理
 */
package org.example.servicecommon.redisKey;

public class BaseMessageKey {

    /**
     * Redis 广播频道名称（发布/订阅模式）
     * 用于后端节点之间的消息广播分发
     * 建议通过 convertAndSend 发送结构化 JSON 消息
     * 示例：redisTemplate.convertAndSend(MESSAGE_BROADCAST_CHANNEL, jsonPayload)
     */
    public static final String MESSAGE_BROADCAST_CHANNEL = CommonKey.KEY + "message:sse:broadcast";

    /**
     * Redis 中用于标记用户在线状态的键前缀
     * 格式为：message:sse:online:{userId}
     * 说明：
     * - 用户建立 SSE 连接时设置该键表示在线状态，并设置合理过期时间（如5分钟）；
     * - 通过续期机制维持在线状态，过期即视为离线；
     * - 各服务节点可通过此键判断用户是否在线，辅助离线消息处理逻辑。
     */
    public static final String ONLINE_USER_KEY_PREFIX = CommonKey.KEY + "message:sse:online:";

    /**
     * Redis 中用于缓存离线用户的待推送消息列表
     * 格式为：{前缀}message:offline:list:{userId} -> List<String>（消息JSON）
     * 说明：
     * - 用户离线时可将推送消息暂存到该列表；
     * - 用户重新连接时从列表读取并推送；
     * - 建议结合 List/Stream 数据结构控制容量，防止爆炸。
     */
    public static final String OFFLINE_MESSAGE_LIST_KEY = CommonKey.KEY + "message:offline:list:";
}