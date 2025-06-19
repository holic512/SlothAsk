/**
 * File Name: AnswerAiKey.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-19
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.redisKey;

public class AnswerAiKey {
    /**
     * Redis 中用于标记某条回答是否正在进行 AI 解析的状态锁
     * 格式为：{前缀}answer:ai:parsing:{answerId} -> 1
     * 说明：设置该键后表示该回答处于 AI 解析中，可用于控制重复提交
     * 推荐设置有效期，例如 5~10 分钟，防止死锁
     */
    public static final String ANSWER_AI_PARSING_KEY = CommonKey.KEY + "answer:ai:parsing:";
}
