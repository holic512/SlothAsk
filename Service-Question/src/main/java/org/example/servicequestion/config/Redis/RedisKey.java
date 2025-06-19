/**
 * RedisKey 类用于存储和管理与 Redis 相关的键的常量。
 * 该类主要包含用于缓存题目虚拟 ID 的 Redis 键前缀。
 * 通过该类，其他类可以方便地获取 Redis 中存储数据所用的键值。
 * <p>
 * 本类的设计遵循单一责任原则，专注于提供 Redis 键前缀的管理。
 * </p>
 *
 * @author holic512
 * @version 1.0
 * @since 2025-04-22
 */
package org.example.servicequestion.config.Redis;

/**
 * RedisKey 类用于存储和管理与 Redis 相关的键的常量。
 */
public class RedisKey {
    /**
     * Redis 中用于缓存题目虚拟 ID 的键前缀
     * 格式为：{前缀}Question:VId:{真实题目ID} -> 虚拟ID
     * * 格式为：{前缀}Question:VId:{虚拟ID} -> 真实题目ID
     * 用于题目展示时对真实ID进行脱敏处理
     */
    public static final String QUESTION_VID_KEY = RedisConfig.getKey() + "Question:VId:";

    /**
     * Redis 中用于缓存用户收藏的题目列表
     * 格式为：{前缀}fav:question:user:{用户ID} -> ZSet<题目ID><时间戳>
     * 用于快速判断用户是否收藏某道题，以及获取用户的所有收藏题目
     */
    public static final String FAV_QUESTION_USER_KEY = RedisConfig.getKey() + "fav:question:user:";


    /**
     * Redis 中用于标记某条回答是否正在进行 AI 解析的状态锁
     * 格式为：{前缀}answer:ai:parsing:{answerId} -> 1
     * 说明：设置该键后表示该回答处于 AI 解析中，可用于控制重复提交
     * 推荐设置有效期，例如 5~10 分钟，防止死锁
     */
    public static final String ANSWER_AI_PARSING_KEY = RedisConfig.getKey() + "answer:ai:parsing:";
}
