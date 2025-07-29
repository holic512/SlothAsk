/**
 * File Name: QuestionKey.java
 * Description: 题目相关的 Redis 键常量定义类，用于题目虚拟ID与真实ID的映射等缓存逻辑
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 该类用于集中管理题目相关 Redis 键前缀，统一命名规范，便于维护和扩展。
 */
package org.example.servicecommon.redisKey;

public class QuestionKey {

    /**
     * Redis 中用于缓存题目虚拟 ID 的键前缀
     * 格式为：{前缀}Question:VId:{真实题目ID} -> 虚拟ID
     * 格式为：{前缀}Question:VId:{虚拟ID} -> 真实题目ID
     * 用于题目展示时对真实ID进行脱敏处理
     */
    public static final String QUESTION_VID_KEY = CommonKey.KEY + "Question:VId:";

}
