/**
 * File Name: PostUserSignServiceImpl.java
 * Description: 用户登录注册服务实现类，处理验证码发送和验证的业务逻辑
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * 实现用户登录和注册过程中的验证码生成、发送、验证等功能，支持邮箱验证码登录和新用户注册流程
 */
package org.example.serviceuser.user.sign.service.Impl;

import java.util.concurrent.TimeUnit;

import org.example.serviceuser.config.Redis.RedisConfig;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.feign.MailFeign;
import org.example.serviceuser.user.sign.enums.PostUserSignEnum;
import org.example.serviceuser.user.sign.mapper.UserSignUserMapper;
import org.example.serviceuser.user.sign.request.VerifySignVerificationCodeRequest;
import org.example.serviceuser.user.sign.service.PostUserSignService;
import org.example.serviceuser.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

/**
 * 用户登录注册服务实现类
 * 实现用户通过邮箱验证码进行登录和注册的相关业务逻辑
 */
@Service
public class PostUserSignServiceImpl implements PostUserSignService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MailFeign mailFeign;
    private final UserSignUserMapper userSignUserMapper;

    // 代表着用作于 验证
    private static final String VER_PREFIX = RedisConfig.getKey() + "verification:";

    // 用于待注册用户的 邮箱 存储
    private static final String PENDING_REGISTRATION = RedisConfig.getKey() + "pending_registration_email:";

    /**
     * 构造函数，通过依赖注入获取所需服务和组件
     * 
     * @param redisTemplate Redis模板，用于存储验证码
     * @param mailFeign 邮件服务客户端，用于发送验证码邮件
     * @param userSignUserMapper 用户数据访问接口，用于查询用户信息
     */
    @Autowired
    public PostUserSignServiceImpl(RedisTemplate<String, Object> redisTemplate, MailFeign mailFeign, UserSignUserMapper userSignUserMapper) {
        this.redisTemplate = redisTemplate;
        this.mailFeign = mailFeign;
        this.userSignUserMapper = userSignUserMapper;
    }

    /**
     * 发送登录/注册验证码
     * 生成6位数字验证码并通过邮件发送给用户，验证码有效期为5分钟
     * 
     * @param mail 用户邮箱地址
     * @return boolean 返回验证码发送结果，true表示成功，false表示失败
     */
    @Override
    public boolean sendSignVerificationCode(String mail) {
        try {
            // 生成验证码
            String VerCode = UuidUtil.generate6DigitNumericId();

            // 将此 mail作为key 插入 redis 五分钟过期时间
            redisTemplate.opsForValue().set(
                    VER_PREFIX + mail,
                    VerCode, // 获取运算结果
                    5,
                    TimeUnit.MINUTES
            );

            // 发送邮件
            mailFeign.sendVerificationEmail(mail, mail, "登录或注册", VerCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 验证登录/注册验证码
     * 验证用户提交的邮箱验证码，根据验证结果和用户注册状态返回不同的处理结果
     * 
     * @param request 包含邮箱和验证码的请求对象
     * @return Pair<PostUserSignEnum, String> 返回验证结果和附加信息
     *         - SUCCESS_LOGIN: 验证成功且用户已注册，返回token
     *         - SUCCESS_BUT_NOT_REGISTERED: 验证成功但用户未注册，返回临时UUID用于后续注册
     *         - INVALID_VERIFICATION_CODE: 验证失败，验证码错误或已过期
     */
    @Override
    public Pair<PostUserSignEnum, String> verifySignVerificationCode(VerifySignVerificationCodeRequest request) {
        // 读取数据
        String email = request.getEmail();
        String code = request.getCode();


        // 获取该邮箱下的验证码
        String realValidateCode = (String) redisTemplate.opsForValue().get(VER_PREFIX + email);

        // 当验证码为空
        if (realValidateCode == null) return Pair.of(PostUserSignEnum.INVALID_VERIFICATION_CODE, "null");

        // 判断验证码是否正确
        if (!realValidateCode.equals(code))
            return Pair.of(PostUserSignEnum.INVALID_VERIFICATION_CODE, "null");

        // 验证码正确，删除 Redis 中的验证码
        redisTemplate.delete(VER_PREFIX + email);

        // 查询 该邮箱是否注册
        User user = userSignUserMapper.getUserByEmail(email);

        // 当user为空 则证明没有注册 插入临时注册数据并且保存
        if (user == null) {
            // 创建拟注册 uid 并插入redis
            String uid = UuidUtil.generateUuid();

            // 将此 mail作为key 插入 redis 五分钟过期时间
            redisTemplate.opsForValue().set(
                    PENDING_REGISTRATION + email,
                    uid, // 获取运算结果
                    5,
                    TimeUnit.MINUTES
            );

            return Pair.of(PostUserSignEnum.SUCCESS_BUT_NOT_REGISTERED, uid);
        }

        // 反之登录成功-执行登录并且返回token
        return Pair.of(PostUserSignEnum.SUCCESS_LOGIN, "token");
    }
}
