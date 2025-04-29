/**
 * File Name: RedisZSetService.java
 * Description: Redis ZSet 操作服务，提供对 Redis 有序集合（ZSet）的常用操作，
 * 包括单值及批量的增、删，以及查询功能，内置参数校验以提高健壮性与安全性。
 * Author: holic512
 * Created Date: 2025-04-23
 * Version: 1.1
 * Usage:
 * // 添加单个元素
 * redisZSetService.addToZSet("myzset", myValue, 1.0);
 * // 批量添加元素
 * Map<Object, Double> map = Map.of(value1, 1.0, value2, 2.0);
 * long addedCount = redisZSetService.batchAddToZSet("myzset", map);
 */
package org.example.servicequestion.config.Redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Redis 有序集合（ZSet）操作服务类。
 * <p>
 * 本服务封装了对 ZSet 的增删改查功能，包括单值与批量操作，
 * 并在操作前进行参数合法性校验，以防止非法调用。
 * </p>
 */
@Service
public class RedisZSetService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 构造函数，注入 RedisTemplate
     *
     * @param redisTemplate Spring Data Redis 模板，用于执行 Redis 操作
     */
    @Autowired
    public RedisZSetService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向指定的 ZSet 添加单个元素。
     *
     * @param key   ZSet 的 Redis 键，非空且不全为空白
     * @param value 要添加的元素，必须可序列化且非空
     * @param score 元素的分值，用于 ZSet 中的排序
     * @throws IllegalArgumentException 当 key 或 value 不合法时抛出
     * @throws IllegalStateException    当 Redis 操作失败时抛出
     */
    public void addToZSet(String key, Object value, double score) {
        // 校验键的合法性
        validateKey(key);

        // 校验值是否为空
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        // 校验值是否可序列化，假如有必要可以自己实现或使用序列化工具来验证
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException("Value must be serializable");
        }

        try {
            // 执行 Redis 操作
            redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to add value to ZSet", e);
        }
    }

    /**
     * 向指定的 ZSet 添加单个元素，并设置过期时间。
     *
     * @param key        ZSet 的 Redis 键，非空且不全为空白
     * @param value      要添加的元素，必须可序列化且非空
     * @param score      元素的分值，用于 ZSet 中的排序
     * @param expireTime 过期时间（秒），必须大于零
     * @throws IllegalArgumentException 当 key、value 或 expireTime 不合法时抛出
     * @throws IllegalStateException    当 Redis 操作失败时抛出
     */
    public void addToZSetWithExpire(String key, Object value, double score, long expireTime) {
        // 校验键的合法性
        validateKey(key);

        // 校验值是否为空
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        // 校验值是否可序列化
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException("Value must be serializable");
        }

        // 校验过期时间
        if (expireTime <= 0) {
            throw new IllegalArgumentException("Expire time must be greater than zero");
        }

        try {
            // 执行 Redis 添加元素操作
            redisTemplate.opsForZSet().add(key, value, score);
            // 设置过期时间
            redisTemplate.expire(key, Duration.ofSeconds(expireTime));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to add value to ZSet with expire time", e);
        }
    }


    /**
     * 批量向 ZSet 添加多个元素。
     *
     * @param key           ZSet 的 Redis 键，非空且不全为空白
     * @param valueScoreMap 元素与分值映射，不能为空或包含 null
     * @return 实际新增的元素数量，如果映射为空或所有元素已存在返回 0
     * @throws IllegalArgumentException 当 key、映射为空或包含 null 时抛出
     */
    public long batchAddToZSet(String key, Map<Object, Double> valueScoreMap) {
        validateKey(key);
        if (valueScoreMap == null || valueScoreMap.isEmpty()) {
            throw new IllegalArgumentException("valueScoreMap must not be null or empty");
        }
        Set<TypedTuple<Object>> tuples = new HashSet<>();
        for (Map.Entry<Object, Double> entry : valueScoreMap.entrySet()) {
            Object value = entry.getKey();
            Double score = entry.getValue();
            if (value == null || score == null) {
                throw new IllegalArgumentException("Neither value nor score can be null");
            }
            tuples.add(new DefaultTypedTuple<>(value, score));
        }
        Long added = redisTemplate.opsForZSet().add(key, tuples);
        return added == null ? 0L : added;
    }

    /**
     * 获取指定 ZSet 的成员集合，按分数升序排列（按索引范围）。
     *
     * @param key   ZSet 的 Redis 键，非空且不全为空白
     * @param start 起始索引（0 为第一个元素）
     * @param end   结束索引（包含该索引对应元素，当 end为-1时表示最后一个）
     * @return 索引范围内的元素集合；当 key 不存在时返回空集合
     * @throws IllegalArgumentException 当 key 不合法时抛出
     */
    public Set<Object> rangeFromZSet(String key, long start, long end) {
        validateKey(key);
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取指定成员在 ZSet 中的分值。
     *
     * @param key   ZSet 的 Redis 键，非空且不全为空白
     * @param value 要查询的成员，非空
     * @return 元素的分值；当成员不存在或 key 不存在时返回 null
     * @throws IllegalArgumentException 当 key 或 value 不合法时抛出
     */
    public Double getScoreFromZSet(String key, Object value) {
        validateKey(key);
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 从 ZSet 中删除指定成员。
     *
     * @param key   ZSet 的 Redis 键，非空且不全为空白
     * @param value 要删除的成员，非空
     * @return 是否成功删除，true 表示至少删除一个元素
     * @throws IllegalArgumentException 当 key 或 value 不合法时抛出
     */
    public boolean removeFromZSet(String key, Object value) {
        validateKey(key);
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        Long result = redisTemplate.opsForZSet().remove(key, value);
        return result != null && result > 0;
    }

    /**
     * 批量从 ZSet 中删除多个成员。
     *
     * @param key    ZSet 的 Redis 键，非空且不全为空白
     * @param values 待删除成员集合，不能为空或包含 null
     * @return 实际删除的成员数量
     * @throws IllegalArgumentException 当 key 或集合非法时抛出
     */
    public long batchRemoveFromZSet(String key, Collection<Object> values) {
        validateKey(key);
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values collection must not be null or empty");
        }
        for (Object value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values collection must not contain null");
            }
        }
        Long removed = redisTemplate.opsForZSet().remove(key, values.toArray());
        return removed == null ? 0L : removed;
    }

    /**
     * 获取指定分数范围内的成员，按分数升序排列。
     *
     * @param key ZSet 的 Redis 键，非空且不全为空白
     * @param min 最小分值（包含）
     * @param max 最大分值（包含）
     * @return 分值区间内的成员集合；当没有符合条件的成员时返回空集合
     * @throws IllegalArgumentException 当 key 不合法时抛出
     */
    public Set<Object> rangeByScoreFromZSet(String key, double min, double max) {
        validateKey(key);
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 验证 Redis 键的合法性，避免空或仅包含空白字符。
     *
     * @param key Redis 键
     * @throws IllegalArgumentException 当 key 为 null 或空白时抛出
     */
    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Redis key must not be null or empty");
        }
    }
}
