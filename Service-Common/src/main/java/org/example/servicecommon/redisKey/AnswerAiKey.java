/**
 * File Name: AnswerAiKey.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-19
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicecommon.redisKey;

public class AnswerAiKey {
    /**
     * Redis 中用于标记某条回答是否正在进行 AI 解析的状态锁
     * 格式为：{前缀}answer:ai:parsing:{answerId} -> 1
     * 说明：设置该键后表示该回答处于 AI 解析中，可用于控制重复提交
     * 推荐设置有效期，例如 5~10 分钟，防止死锁
     */
    public static final String ANSWER_AI_PARSING_KEY = CommonKey.KEY + "answer:ai:parsing:";

    /**
     * Redis 中用于记录用户当天提交次数的缓存键前缀
     * 格式为：{前缀}submit:count:{userId}:{yyyyMMdd} -> 整数（提交次数）
     * 示例：app:submit:count:12345:20250623 -> 5
     * 说明：
     * - 每次用户提交时，先使用 INCR 更新此键的值；
     * - 此键在每日 0 点时由定时任务统一汇总入库并删除；
     * - 可设置键过期时间为 2~3 天，防止长期积压；
     * 用途：用于减少数据库写压力，实现每日次数统计缓存。
     */
    public static final String DAILY_SUBMIT_COUNT_KEY = CommonKey.KEY + "answerSubmit:count:";

}
