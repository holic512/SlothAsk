/**
 * File Name: IdConversionService.java
 * Description: 提供原始ID与加密ID（VirtualId）之间的互相转换服务，支持通过 Redis 缓存加速解析。
 * Author: holic512
 * Created Date: 2025-04-24
 * Version: 1.0
 * Usage:
 * 可在需要对外隐藏真实ID的业务场景中使用，调用 getOriginalIdFromVirtualId 或 getVirtualIdFromOriginalId 方法进行转换，
 * 支持双向缓存，减少重复计算和数据库查询。
 */

package org.example.servicequestion.user.commonService;

import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IdConversionService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    IdConversionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 根据VirtualId获取原始ID
    public Long getOriginalIdFromVirtualId(String virtualId) {
        String originalIdStr = (String) redisTemplate.opsForValue().get(RedisKey.QUESTION_VID_KEY + virtualId);
        Long originalId = null;

        if (originalIdStr != null) {
            // 清理潜在的双引号（如 Redis 中存的是 "\"1\""）
            String cleaned = originalIdStr.replaceAll("^\"|\"$", "");
            if (cleaned.matches("\\d+")) {
                originalId = Long.parseLong(cleaned);
            }
        }

        if (originalId == null) {
            originalId = IdEncryptor.decryptId(virtualId);
            if (originalId != null) {
                redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + originalId, virtualId, 1, TimeUnit.DAYS);
            }
        }

        return originalId;
    }

    // 根据原始ID获取VirtualId
    public String getVirtualIdFromOriginalId(Long originalId) {
        String virtualId = (String) redisTemplate.opsForValue().get(RedisKey.QUESTION_VID_KEY + originalId);

        if (virtualId == null) {
            virtualId = IdEncryptor.encryptId(originalId);
            if (virtualId != null) {
                // 缓存双向映射
                redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + originalId, virtualId, 1, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
            }
        }

        return virtualId;
    }


}
