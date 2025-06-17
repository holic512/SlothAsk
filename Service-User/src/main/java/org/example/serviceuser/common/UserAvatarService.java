/**
 * File Name: UserAvatarService.java
 * Description: 封装获取用户头像 URL 的通用服务，支持 #占位解析 + 缓存机制。
 * Author: holic512
 * Created Date: 2025-06-13
 * Version: 1.1
 */

package org.example.serviceuser.common;

import org.example.serviceuser.config.Redis.RedisConstants;
import org.example.serviceuser.feign.ServiceImageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户头像服务类，用于统一处理头像URL获取逻辑。
 * 如果头像URL以 # 开头，则视为占位符，需通过 ImageService 微服务获取真实 URL，并写入 Redis 缓存。
 */
@Service
public class UserAvatarService {

    private final ServiceImageFeign serviceImageFeign;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserAvatarService(ServiceImageFeign serviceImageFeign, RedisTemplate<String, Object> redisTemplate) {
        this.serviceImageFeign = serviceImageFeign;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取用户头像的真实 URL。
     *
     * @param avatarUrl 用户头像地址，如以 "#" 开头表示占位符（如 "#avatar_abc.jpg"）
     * @return 可直接访问的真实 URL；若解析失败，则返回原 avatarUrl。
     */
    public String getUserAvatar(final String avatarUrl) {
        if (avatarUrl == null || !avatarUrl.startsWith("#")) {
            return avatarUrl;
        }

        String fileName = avatarUrl.substring(1);
        String cacheKey = RedisConstants.User.Avatar.USER_AVATAR_URL_PREFIX + fileName;

        // 先查缓存
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached instanceof String cachedUrl) {
            return cachedUrl;
        }

        // 调用远程服务获取 URL
        try {
            String realUrl = serviceImageFeign.getPreviewUrl(fileName);
            if (realUrl != null) {
                redisTemplate.opsForValue().set(
                        cacheKey,
                        realUrl,
                        RedisConstants.User.Avatar.getUserAvatarUrlTtlWithJitter()  // 带 ±60 秒抖动
                );
                return realUrl;
            }
        } catch (Exception e) {
            // 日志建议在真实场景中打印
            System.err.println("获取头像失败：" + e.getMessage());
        }

        // 兜底返回原始URL
        return avatarUrl;
    }

}
