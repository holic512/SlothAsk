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

public class RedisConstants {
    /**
     * 用户缓存相关
     */
    public static class User {

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
}
