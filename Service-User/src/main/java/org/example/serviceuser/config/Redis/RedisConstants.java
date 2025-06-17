/**
 * File Name: RedisConstants.java
 * Description: Redis 缓存常量配置
 * Author: holic512
 * Created Date: 2025-05-10
 * Version: 1.1
 * Usage:
 * 定义与 Redis 缓存键值及过期时间相关的常量
 */
package org.example.serviceuser.config.Redis;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class RedisConstants {
    /**
     * 用户缓存相关
     */
    public static class User {

        /**
         * 用户基本信息缓存前缀
         * 缓存 key 示例：cache:user:info:{userId}
         */
        public static final String USER_INFO_PREFIX = RedisConfig.key + "cache:user:info:";
        /**
         * 用户基本信息缓存有效时间（15分钟）
         */
        public static final Duration USER_INFO_TTL = Duration.ofMinutes(15);

        /**
         * 获取带 ±60 秒抖动的用户信息缓存 TTL
         */
        public static Duration getUserInfoTtlWithJitter() {
            int jitter = ThreadLocalRandom.current().nextInt(-60, 61); // ±60秒
            return User.USER_INFO_TTL.plusSeconds(jitter);
        }

        /**
         * 用户头像相关缓存
         */
        public static class Avatar {
            /**
             * 用户头像 URL 缓存前缀 -
             * 当用户头像以 '#' 开头时，表示为占位符，需要异步调用 ImageService 微服务获取真实 URL 并缓存。
             * 缓存 key 示例：cache:user:avatar_url:{fileName}
             */
            public static final String USER_AVATAR_URL_PREFIX = RedisConfig.key + "cache:user:avatar_url:";
            /**
             * 用户头像 URL 缓存有效时间（10分钟） -
             * 为防止所有缓存同时失效导致雪崩，建议在设置缓存时加入 ±60秒的随机偏移。
             */
            public static final Duration USER_AVATAR_URL_TTL = Duration.ofMinutes(10);

            /**
             * 获取带 ±60 秒抖动的用户头像缓存 TTL
             */
            public static Duration getUserAvatarUrlTtlWithJitter() {
                int jitter = ThreadLocalRandom.current().nextInt(-60, 61); // ±60秒
                return User.Avatar.USER_AVATAR_URL_TTL.plusSeconds(jitter);
            }
        }


    }

    /**
     * 邮箱验证相关
     */
    public static class Email {

        /**
         * 邮箱更改验证码缓存前缀
         */
        public static final String EMAIL_CHANGE_CODE_PREFIX = RedisConfig.key + "cache:email:email_change:";
        /**
         * 邮箱更改验证码过期时间（10分钟）
         */
        public static final Duration EMAIL_CHANGE_CODE_TTL = Duration.ofMinutes(10);

        /**
         * 密码重置验证码缓存前缀
         */
        public static final String PASSWORD_RESET_CODE_PREFIX = RedisConfig.key + "cache:email:password_reset:";
        /**
         * 密码重置验证码过期时间（10分钟）
         */
        public static final Duration PASSWORD_RESET_CODE_TTL = Duration.ofMinutes(10);

        /**
         * GitHub 绑定验证码缓存前缀
         */
        public static final String GITHUB_BIND_CODE_PREFIX = RedisConfig.key + "cache:email:github_bind:";
        /**
         * GitHub 绑定验证码过期时间（10分钟）
         */
        public static final Duration GITHUB_BIND_CODE_TTL = Duration.ofMinutes(10);

        /**
         * GitHub 绑定编辑态缓存前缀
         */
        public static final String GITHUB_BIND_EDITING_PREFIX = RedisConfig.key + "cache:github:bind:editing:";
        /**
         * GitHub 绑定编辑态过期时间（5分钟）
         */
        public static final Duration GITHUB_BIND_EDITING_TTL = Duration.ofMinutes(5);

        private Email() {
        }
    }

    /**
     * 评论相关缓存
     */
    public static class Comment {
        /**
         * 用户评论列表缓存前缀
         * 缓存 key 示例：cache:user:comments:{userId}:{page}:{size}
         */
        public static final String USER_COMMENTS_PREFIX = RedisConfig.key + "cache:user:comments:";
        
        /**
         * 用户评论列表缓存有效时间（5分钟）
         */
        public static final Duration USER_COMMENTS_TTL = Duration.ofMinutes(5);

        /**
         * 获取带 ±30 秒抖动的评论缓存 TTL
         */
        public static Duration getUserCommentsTtlWithJitter() {
            int jitter = ThreadLocalRandom.current().nextInt(-30, 31); // ±30秒
            return Comment.USER_COMMENTS_TTL.plusSeconds(jitter);
        }
    }

    /**
     * 收藏相关缓存
     */
    public static class Favorite {
        /**
         * 用户收藏问题列表缓存前缀
         * 缓存 key 示例：cache:user:favorites:{userId}:{page}:{size}
         */
        public static final String USER_FAVORITES_PREFIX = RedisConfig.key + "cache:user:favorites:";
        
        /**
         * 用户收藏问题列表缓存有效时间（5分钟）
         */
        public static final Duration USER_FAVORITES_TTL = Duration.ofMinutes(5);

        /**
         * 获取带 ±30 秒抖动的收藏缓存 TTL
         */
        public static Duration getUserFavoritesTtlWithJitter() {
            int jitter = ThreadLocalRandom.current().nextInt(-30, 31); // ±30秒
            return Favorite.USER_FAVORITES_TTL.plusSeconds(jitter);
        }
    }
}
