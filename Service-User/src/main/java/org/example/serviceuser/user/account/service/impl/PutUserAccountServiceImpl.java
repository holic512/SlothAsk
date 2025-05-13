/**
 * File Name: PutUserAccountServiceImpl.java
 * Description: 更新用户账户信息的服务实现类
 * Author: holic512
 * Created Date: 2025-05-20
 * Version: 1.0
 * Usage:
 * 实现用户账户信息更新的服务接口，包括修改用户名和邮箱
 */
package org.example.serviceuser.user.account.service.impl;

import org.example.serviceuser.config.Redis.RedisConfig;
import org.example.serviceuser.config.Redis.RedisConstants;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserAuth;
import org.example.serviceuser.feign.MailFeign;
import org.example.serviceuser.user.account.mapper.UserAccountMapper;
import org.example.serviceuser.user.account.service.PutUserAccountService;
import org.example.serviceuser.user.sign.mapper.UserAuthMapper;
import org.example.serviceuser.util.SCryptUtil;
import org.example.serviceuser.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 更新用户账户信息的服务实现类
 */
@Service
public class PutUserAccountServiceImpl implements PutUserAccountService {

    // 用户名修改次数前缀
    private static final String USERNAME_CHANGE_PREFIX = RedisConfig.key + "username_change:";
    // 每月用户名修改最大次数
    private static final int MAX_USERNAME_CHANGES_PER_MONTH = 3;
    private final UserAccountMapper userAccountMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MailFeign mailFeign;
    private final UserAuthMapper userAuthMapper;

    /**
     * 构造函数，通过依赖注入获取所需服务和组件
     *
     * @param userAccountMapper 用户账户数据访问接口
     * @param redisTemplate     Redis模板，用于存储验证码
     * @param mailFeign         邮件服务客户端，用于发送验证码邮件
     * @param userAuthMapper    用户认证数据访问接口
     */
    @Autowired
    public PutUserAccountServiceImpl(UserAccountMapper userAccountMapper,
                                     RedisTemplate<String, Object> redisTemplate,
                                     MailFeign mailFeign,
                                     UserAuthMapper userAuthMapper) {
        this.userAccountMapper = userAccountMapper;
        this.redisTemplate = redisTemplate;
        this.mailFeign = mailFeign;
        this.userAuthMapper = userAuthMapper;
    }


    @Override
    public Boolean selectUsernameExists(String username) {
        Long count = userAccountMapper.selectUsernameExists(username);
        return count != null && count > 0;
    }

    /**
     * 获取当前月份的Redis键
     *
     * @param userId 用户ID
     * @return Redis键
     */
    private String getUsernameChangeKey(Long userId) {
        // 获取当前年月，格式为YYYY-MM
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return USERNAME_CHANGE_PREFIX + userId + ":" + yearMonth;
    }

    /**
     * 获取用户名剩余修改次数
     *
     * @param userId 用户ID
     * @return 剩余修改次数
     */
    @Override
    public int getRemainingUsernameChanges(Long userId) {
        // 检查用户是否存在
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return 0;
        }

        // 获取当前月份的Redis键
        String redisKey = getUsernameChangeKey(userId);

        // 获取已使用的修改次数
        Integer usedChanges = (Integer) redisTemplate.opsForValue().get(redisKey);

        // 如果没有记录，表示本月还没有修改过
        if (usedChanges == null) {
            return MAX_USERNAME_CHANGES_PER_MONTH;
        }

        // 计算剩余次数
        int remaining = MAX_USERNAME_CHANGES_PER_MONTH - usedChanges;
        return Math.max(0, remaining);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUsername(Long userId, String username) {
        // 检查用户是否存在
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 检查剩余修改次数
        int remainingChanges = getRemainingUsernameChanges(userId);
        if (remainingChanges <= 0) {
            return false; // 已达到本月修改次数上限
        }

        // 获取当前月份的Redis键
        String redisKey = getUsernameChangeKey(userId);

        // 获取已使用的修改次数
        Integer usedChanges = (Integer) redisTemplate.opsForValue().get(redisKey);

        // 更新用户名
        user.setUsername(username);
        int result = userAccountMapper.updateById(user);

        if (result > 0) {
            // 更新成功，增加已使用的修改次数
            if (usedChanges == null) {
                usedChanges = 0;
            }

            // 更新Redis中的使用次数
            redisTemplate.opsForValue().set(redisKey, usedChanges + 1);

            // 设置过期时间到下个月初
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // 计算从现在到下个月初的秒数
            long expireSeconds = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
            redisTemplate.expire(redisKey, expireSeconds, TimeUnit.SECONDS);

            return true;
        }

        return false;
    }


    /**
     * 初始化用户密码
     * 仅当用户密码为null或空字符串时才能设置，否则返回失败
     *
     * @param userId   用户ID
     * @param password 新密码
     * @return 更新结果，true表示成功，false表示失败或密码已设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initializePassword(Long userId, String password) {
        // 检查用户是否存在
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return false; // 用户不存在
        }

        // 检查密码是否为空
        String currentPassword = user.getPassword();
        if (currentPassword != null && !currentPassword.isEmpty()) {
            return false; // 密码已设置，不能使用初始化接口
        }

        // 使用SCryptUtil加密密码
        String encryptedPassword = SCryptUtil.hashPassword(password);

        // 更新用户密码
        user.setPassword(encryptedPassword);
        int result = userAccountMapper.updateById(user);

        return result > 0;
    }

    /**
     * 通过验证旧密码修改用户密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 更新结果代码
     * 1: 密码修改成功
     * 0: 旧密码错误
     * -1: 用户不存在
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePasswordWithOldPassword(Long userId, String oldPassword, String newPassword) {
        // 检查用户是否存在
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return -1; // 用户不存在
        }

        // 获取当前存储的密码哈希
        String currentPassword = user.getPassword();

        // 空密码不能使用此方法修改
        if (currentPassword == null || currentPassword.isEmpty()) {
            return 0; // 等同于密码错误，应使用初始化密码接口
        }

        // 验证旧密码
        boolean passwordMatch = SCryptUtil.verifyPassword(oldPassword, currentPassword);
        if (!passwordMatch) {
            return 0; // 旧密码错误
        }

        // 使用SCryptUtil加密新密码
        String encryptedPassword = SCryptUtil.hashPassword(newPassword);

        // 更新用户密码
        user.setPassword(encryptedPassword);
        int result = userAccountMapper.updateById(user);

        return result > 0 ? 1 : 0;
    }

    /**
     * 发送密码重置验证码
     * 向用户绑定的邮箱发送验证码，用于重置密码
     *
     * @param userId 用户ID
     * @return 包含键值和脱敏邮箱的Map，失败则返回null
     */
    @Override
    public Map<String, String> sendPasswordResetCode(Long userId) {
        try {
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return null;
            }

            // 获取用户邮箱
            String email = user.getEmail();
            if (email == null || email.isEmpty()) {
                return null; // 用户未绑定邮箱
            }

            // 生成验证码
            String verCode = UuidUtil.generate6DigitNumericId();
            
            // 生成唯一标识符
            String key = UUID.randomUUID().toString().replace("-", "");

            // 使用Redis Hash结构存储验证码和邮箱，避免复杂序列化问题
            String redisKey = RedisConstants.Email.PASSWORD_RESET_CODE_PREFIX + key;
            redisTemplate.opsForHash().put(redisKey, "code", verCode);
            redisTemplate.opsForHash().put(redisKey, "email", email);
            redisTemplate.expire(redisKey, RedisConstants.Email.PASSWORD_RESET_CODE_TTL);

            // 发送邮件
            mailFeign.sendVerificationEmail(email, email, "密码重置", verCode);

            // 脱敏邮箱
            String maskedEmail = maskEmail(email);
            
            // 返回结果
            return Map.of("key", key, "email", maskedEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对邮箱进行脱敏处理
     * 格式：前三位 + * + @ + 域名
     * 例如：abc***@example.com
     *
     * @param email 原始邮箱
     * @return 脱敏后的邮箱
     */
    private String maskEmail(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];

        // 至少保留3个字符，其余用*替代
        int visibleLength = Math.min(3, name.length());
        StringBuilder maskedName = new StringBuilder(name.substring(0, visibleLength));
        for (int i = visibleLength; i < name.length(); i++) {
            maskedName.append("*");
        }

        return maskedName + "@" + domain;
    }

    /**
     * 发送邮箱更改验证码
     * 向指定邮箱发送验证码，用于验证更改邮箱绑定
     *
     * @param email 目标邮箱地址
     * @return 包含生成的唯一标识符(uid)的Map，失败则返回null
     */
    @Override
    public Map<String, String> sendEmailChangeCode(String email) {
        try {
            // 检查邮箱是否已被使用
            Long count = userAccountMapper.selectEmailExists(email);
            if (count != null && count > 0) {
                return null; // 邮箱已被使用
            }

            // 生成验证码
            String verCode = UuidUtil.generate6DigitNumericId();
            
            // 生成唯一标识符
            String uid = UUID.randomUUID().toString().replace("-", "");

            // 将验证码存入Redis，使用邮箱更改验证码前缀
            String redisKey = RedisConstants.Email.EMAIL_CHANGE_CODE_PREFIX + uid;
            redisTemplate.opsForHash().put(redisKey, "code", verCode);
            redisTemplate.opsForHash().put(redisKey, "email", email);
            redisTemplate.expire(redisKey, RedisConstants.Email.EMAIL_CHANGE_CODE_TTL);

            // 发送邮件
            mailFeign.sendVerificationEmail(email, email, "邮箱绑定验证", verCode);

            // 返回唯一标识符
            return Map.of("uid", uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向用户绑定的原邮箱发送验证码
     * 向用户当前绑定的邮箱发送验证码，用于验证原邮箱所有权
     *
     * @param userId 用户ID
     * @return 包含原邮箱(脱敏)和唯一标识符的Map，失败则返回null
     */
    @Override
    public Map<String, String> sendVerificationToOriginalEmail(Long userId) {
        try {
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return null;
            }

            // 获取用户邮箱
            String email = user.getEmail();
            if (email == null || email.isEmpty()) {
                return null; // 用户未绑定邮箱
            }

            // 生成验证码
            String verCode = UuidUtil.generate6DigitNumericId();
            
            // 生成唯一标识符
            String uid = UUID.randomUUID().toString().replace("-", "");

            // 将验证码和邮箱存入Redis，使用特定前缀
            String redisKey = RedisConstants.Email.EMAIL_CHANGE_CODE_PREFIX + "original:" + uid;
            redisTemplate.opsForHash().put(redisKey, "code", verCode);
            redisTemplate.opsForHash().put(redisKey, "email", email);
            redisTemplate.expire(redisKey, RedisConstants.Email.EMAIL_CHANGE_CODE_TTL);

            // 发送邮件
            mailFeign.sendVerificationEmail(email, email, "邮箱变更验证", verCode);

            // 脱敏邮箱
            String maskedEmail = maskEmail(email);
            
            // 返回结果
            return Map.of("uid", uid, "email", maskedEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 双重验证更新邮箱
     * 需要同时验证原邮箱和新邮箱的所有权
     *
     * @param userId          用户ID
     * @param originalUid     原邮箱的唯一标识符
     * @param originalCode    原邮箱的验证码
     * @param newEmail        新邮箱地址
     * @param newUid          新邮箱的唯一标识符
     * @param newCode         新邮箱的验证码
     * @return 更新结果
     * 1: 更新成功
     * 0: 原邮箱验证码错误
     * -1: 新邮箱验证码错误
     * -2: 原邮箱或新邮箱信息不匹配
     * -3: 用户不存在
     * -4: 新邮箱已被使用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int verifyAndUpdateEmailWithTwoSteps(Long userId, String originalUid, String originalCode, 
                                              String newEmail, String newUid, String newCode) {
        try {
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return -3; // 用户不存在
            }

            // 获取用户当前邮箱
            String currentEmail = user.getEmail();
            if (currentEmail == null || currentEmail.isEmpty()) {
                return -2; // 用户未绑定邮箱，无法验证原邮箱
            }

            // 1. 验证原邮箱
            String originalRedisKey = RedisConstants.Email.EMAIL_CHANGE_CODE_PREFIX + "original:" + originalUid;
            String storedOriginalEmail = (String) redisTemplate.opsForHash().get(originalRedisKey, "email");
            String storedOriginalCode = (String) redisTemplate.opsForHash().get(originalRedisKey, "code");

            // 原邮箱验证信息不存在或已过期
            if (storedOriginalEmail == null || storedOriginalCode == null) {
                return 0; // 原邮箱验证码错误
            }

            // 原邮箱不匹配当前用户邮箱
            if (!storedOriginalEmail.equals(currentEmail)) {
                return -2; // 原邮箱信息不匹配
            }

            // 原邮箱验证码不匹配
            if (!originalCode.equals(storedOriginalCode)) {
                return 0; // 原邮箱验证码错误
            }

            // 2. 验证新邮箱
            String newRedisKey = RedisConstants.Email.EMAIL_CHANGE_CODE_PREFIX + newUid;
            String storedNewEmail = (String) redisTemplate.opsForHash().get(newRedisKey, "email");
            String storedNewCode = (String) redisTemplate.opsForHash().get(newRedisKey, "code");

            // 新邮箱验证信息不存在或已过期
            if (storedNewEmail == null || storedNewCode == null) {
                return -1; // 新邮箱验证码错误
            }

            // 新邮箱不匹配请求的邮箱
            if (!storedNewEmail.equals(newEmail)) {
                return -2; // 新邮箱信息不匹配
            }

            // 新邮箱验证码不匹配
            if (!newCode.equals(storedNewCode)) {
                return -1; // 新邮箱验证码错误
            }

            // 检查新邮箱是否已被其他用户使用
            Long count = userAccountMapper.selectEmailExists(newEmail);
            if (count != null && count > 0) {
                return -4; // 新邮箱已被使用
            }

            // 验证通过，删除Redis中的验证码
            redisTemplate.delete(originalRedisKey);
            redisTemplate.delete(newRedisKey);

            // 更新用户邮箱
            user.setEmail(newEmail);
            int result = userAccountMapper.updateById(user);

            return result > 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -2; // 发生异常
        }
    }

    /**
     * 通过密码验证更新邮箱
     *
     * @param userId   用户ID
     * @param uid      唯一标识符
     * @param email    新邮箱地址
     * @param code     验证码
     * @param password 用户密码
     * @return 更新结果
     * 1: 更新成功
     * 0: 密码错误
     * -1: 验证码无效或已过期
     * -2: 邮箱不匹配
     * -3: 用户不存在
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateEmailWithPassword(Long userId, String uid, String email, String code, String password) {
        try {
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return -3; // 用户不存在
            }

            // 验证用户密码
            String currentPassword = user.getPassword();
            if (currentPassword == null || currentPassword.isEmpty()) {
                return 0; // 用户密码未设置，视为密码错误
            }

            // 验证密码
            boolean passwordMatch = SCryptUtil.verifyPassword(password, currentPassword);
            if (!passwordMatch) {
                return 0; // 密码错误
            }

            // 从Redis获取验证信息
            String redisKey = RedisConstants.Email.EMAIL_CHANGE_CODE_PREFIX + uid;
            String storedEmail = (String) redisTemplate.opsForHash().get(redisKey, "email");
            String storedCode = (String) redisTemplate.opsForHash().get(redisKey, "code");

            // 验证码信息不存在或已过期
            if (storedEmail == null || storedCode == null) {
                return -1; // 验证码无效或已过期
            }

            // 检查邮箱是否匹配
            if (!email.equals(storedEmail)) {
                return -2; // 邮箱不匹配
            }
            
            // 验证码是否匹配
            if (!code.equals(storedCode)) {
                return -1; // 验证码错误
            }

            // 验证通过，删除Redis中的验证码
            redisTemplate.delete(redisKey);

            // 检查邮箱是否已被其他用户使用
            Long count = userAccountMapper.selectEmailExists(email);
            if (count != null && count > 0) {
                return -2; // 邮箱已被使用，视为邮箱不匹配
            }

            // 更新用户邮箱
            user.setEmail(email);
            int result = userAccountMapper.updateById(user);

            return result > 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 发生异常，返回密码错误
        }
    }

    /**
     * 验证验证码并重置密码
     *
     * @param userId      用户ID
     * @param key         唯一标识符
     * @param code        验证码
     * @param newPassword 新密码
     * @return 重置结果
     * 1: 密码重置成功
     * 0: 验证码错误或已过期
     * -1: 验证码与邮箱不匹配
     * -2: 用户不存在
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int verifyCodeAndResetPassword(Long userId, String key, String code, String newPassword) {
        // 检查用户是否存在
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return -2; // 用户不存在
        }

        // 从Redis获取验证码信息
        String redisKey = RedisConstants.Email.PASSWORD_RESET_CODE_PREFIX + key;
        
        // 使用Hash操作获取验证码和邮箱
        String storedCode = (String) redisTemplate.opsForHash().get(redisKey, "code");
        String storedEmail = (String) redisTemplate.opsForHash().get(redisKey, "email");

        // 验证码信息不存在或已过期
        if (storedCode == null || storedEmail == null) {
            return 0; // 验证码错误或已过期
        }

        // 验证码不匹配
        if (!code.equals(storedCode)) {
            return 0; // 验证码错误
        }
        
        // 邮箱与用户绑定邮箱不一致
        if (!storedEmail.equals(user.getEmail())) {
            return -1; // 验证码与邮箱不匹配
        }

        // 验证成功，删除Redis中的验证码
        redisTemplate.delete(redisKey);

        // 使用SCryptUtil加密新密码
        String encryptedPassword = SCryptUtil.hashPassword(newPassword);
        
        // 更新用户密码
        user.setPassword(encryptedPassword);
        int result = userAccountMapper.updateById(user);
        
        return result > 0 ? 1 : 0;
    }

    /**
     * 解绑GitHub账号
     *
     * @param userId 用户ID
     * @return 解绑结果，true为成功，false为失败或未绑定
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindGithub(Long userId) {
        // 检查用户是否存在
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return false; // 用户不存在
        }
        
        // 查找用户的GitHub绑定信息
        UserAuth userAuth = userAuthMapper.findByUserIdAndProvider(userId, "github");
        if (userAuth == null) {
            return false; // 用户未绑定GitHub
        }
        
        // 设置删除标志
        userAuth.setIsDeleted(1);
        
        // 更新记录
        try {
            // 假设UserAuth也使用MyBatis-Plus,可以使用updateById方法
            int result = userAuthMapper.updateById(userAuth);
            return result > 0;
        } catch (Exception e) {
            // 记录异常日志
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查用户是否已绑定GitHub账号
     *
     * @param userId 用户ID
     * @return 是否已绑定，true表示已绑定，false表示未绑定
     */
    @Override
    public boolean isGithubBindExists(Long userId) {
        // 用户不存在则返回false
        User user = userAccountMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // 查询用户的GitHub绑定信息
        UserAuth userAuth = userAuthMapper.findByUserIdAndProvider(userId, "github");
        
        // 存在有效的绑定信息则返回true
        return userAuth != null;
    }
}