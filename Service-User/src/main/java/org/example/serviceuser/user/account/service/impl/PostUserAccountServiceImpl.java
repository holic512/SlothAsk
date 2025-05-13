/**
 * File Name: PostUserAccountServiceImpl.java
 * Description: 创建用户账户信息的服务实现类
 * Author: holic512
 * Created Date: 2025-05-22
 * Version: 1.0
 * Usage:
 * 实现用户账户信息创建的服务接口，包括获取GitHub绑定验证码和验证GitHub绑定码
 */
package org.example.serviceuser.user.account.service.impl;

import org.example.serviceuser.config.Redis.RedisConstants;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserAuth;
import org.example.serviceuser.feign.MailFeign;
import org.example.serviceuser.user.account.mapper.UserAccountMapper;
import org.example.serviceuser.user.account.service.PostUserAccountService;
import org.example.serviceuser.user.account.service.PutUserAccountService;
import org.example.serviceuser.user.sign.dto.GitHubUserDTO;
import org.example.serviceuser.user.sign.enums.AuthProvider;
import org.example.serviceuser.user.sign.mapper.UserAuthMapper;
import org.example.serviceuser.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 创建用户账户信息的服务实现类
 */
@Service
public class PostUserAccountServiceImpl implements PostUserAccountService {

    private final UserAccountMapper userAccountMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MailFeign mailFeign;
    private final PutUserAccountService putUserAccountService;
    private final UserAuthMapper userAuthMapper;

    /**
     * 构造函数，通过依赖注入获取所需服务和组件
     *
     * @param userAccountMapper    用户账户数据访问接口
     * @param redisTemplate        Redis模板，用于存储验证码
     * @param mailFeign            邮件服务客户端，用于发送验证码邮件
     * @param putUserAccountService 更新用户账户信息的服务接口
     * @param userAuthMapper       用户认证数据访问接口
     */
    @Autowired
    public PostUserAccountServiceImpl(UserAccountMapper userAccountMapper,
                               RedisTemplate<String, Object> redisTemplate,
                               MailFeign mailFeign,
                               PutUserAccountService putUserAccountService,
                               UserAuthMapper userAuthMapper) {
        this.userAccountMapper = userAccountMapper;
        this.redisTemplate = redisTemplate;
        this.mailFeign = mailFeign;
        this.putUserAccountService = putUserAccountService;
        this.userAuthMapper = userAuthMapper;
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
     * 发送GitHub绑定验证码
     * 根据用户ID查询邮箱并发送验证码邮件
     *
     * @param userId 用户ID
     * @return 包含唯一标识符和脱敏邮箱的Map，失败则返回null
     */
    @Override
    public Map<String, String> sendGithubBindCode(Long userId) {
        try {
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return null;
            }

            // 检查用户是否已绑定GitHub账号
            boolean githubBound = putUserAccountService.isGithubBindExists(userId);
            if (githubBound) {
                return null; // 用户已绑定GitHub账号
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

            // 将验证码和邮箱存入Redis，使用GitHub绑定验证码前缀
            String redisKey = RedisConstants.Email.GITHUB_BIND_CODE_PREFIX + uid;
            redisTemplate.opsForHash().put(redisKey, "code", verCode);
            redisTemplate.opsForHash().put(redisKey, "email", email);
            redisTemplate.expire(redisKey, RedisConstants.Email.GITHUB_BIND_CODE_TTL);

            // 发送邮件
            mailFeign.sendVerificationEmail(email, email, "GitHub账号绑定验证", verCode);

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
     * 验证GitHub绑定验证码
     * 验证成功后在Redis中标记用户正在进行GitHub绑定
     *
     * @param userId 用户ID
     * @param uid    唯一标识符
     * @param code   验证码
     * @return 验证结果
     * 1: 验证成功
     * 0: 验证码错误或已过期
     * -1: 验证码与邮箱不匹配
     * -2: 用户不存在
     * -3: 用户已绑定GitHub账号
     */
    @Override
    public int verifyGithubBindCode(Long userId, String uid, String code) {
        try {
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return -2; // 用户不存在
            }

            // 检查用户是否已绑定GitHub账号
            boolean githubBound = putUserAccountService.isGithubBindExists(userId);
            if (githubBound) {
                return -3; // 用户已绑定GitHub账号
            }

            // 获取用户邮箱
            String email = user.getEmail();
            if (email == null || email.isEmpty()) {
                return -1; // 用户未绑定邮箱，无法验证
            }

            // 从Redis获取验证码信息
            String redisKey = RedisConstants.Email.GITHUB_BIND_CODE_PREFIX + uid;
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
            if (!storedEmail.equals(email)) {
                return -1; // 验证码与邮箱不匹配
            }

            // 验证成功，删除Redis中的验证码
            redisTemplate.delete(redisKey);

            // 在Redis中设置编辑态标记，标明用户正在进行GitHub绑定
            String editingKey = RedisConstants.Email.GITHUB_BIND_EDITING_PREFIX + userId;
            redisTemplate.opsForValue().set(editingKey, true);
            redisTemplate.expire(editingKey, RedisConstants.Email.GITHUB_BIND_EDITING_TTL);
            
            return 1; // 验证成功
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 发生异常，返回验证码错误
        }
    }
    
    /**
     * 绑定GitHub账号到用户
     * 如果该GitHub账号已绑定其他用户，删除原有绑定并重新绑定
     *
     * @param userId 用户ID
     * @param githubUser GitHub用户信息
     * @return 绑定结果，true表示成功，false表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindGithubAccount(Long userId, GitHubUserDTO githubUser) {
        try {
            // 验证参数
            if (userId == null || githubUser == null || githubUser.getId() == null) {
                return false;
            }
            
            // 检查用户是否存在
            User user = userAccountMapper.selectById(userId);
            if (user == null) {
                return false;
            }
            
            // 检查用户是否已绑定GitHub账号
            boolean githubBound = putUserAccountService.isGithubBindExists(userId);
            if (githubBound) {
                return false; // 用户已绑定GitHub账号，不能重复绑定
            }
            
            String provider = AuthProvider.GITHUB.getValue();
            String openId = githubUser.getId().toString(); // GitHub用户ID作为openId
            
            // 检查GitHub账号是否已绑定其他用户
            UserAuth existingAuth = userAuthMapper.findByProviderAndOpenId(provider, openId);
            if (existingAuth != null) {
                // 如果已绑定其他用户，先标记为删除
                existingAuth.setIsDeleted(1); // 标记为已删除
                existingAuth.setUpdatedAt(LocalDateTime.now());
                userAuthMapper.updateById(existingAuth);
            }
            
            // 创建新的绑定记录
            UserAuth newAuth = new UserAuth();
            newAuth.setUserId(userId);
            newAuth.setProvider(provider);
            newAuth.setOpenId(openId);
            newAuth.setStatus(1); // 正常状态
            newAuth.setIsDeleted(0); // 未删除
            newAuth.setCreatedAt(LocalDateTime.now());
            newAuth.setUpdatedAt(LocalDateTime.now());
            
            // 保存新绑定关系
            int result = userAuthMapper.insert(newAuth);
            
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 