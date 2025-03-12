/**
 * File Name: PostUserSignService.java
 * Description: 用户登录注册服务接口，定义验证码发送和验证的业务方法
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * 提供用户登录和注册过程中的验证码处理相关功能接口，包括验证码发送和验证
 */
package org.example.serviceuser.user.sign.service;

import org.example.serviceuser.user.sign.enums.PostUserSignEnum;
import org.example.serviceuser.user.sign.request.RegisterRequest;
import org.example.serviceuser.user.sign.request.VerifySignVerificationCodeRequest;
import org.springframework.data.util.Pair;

/**
 * 用户登录注册服务接口
 * 定义用户通过邮箱验证码进行登录和注册的相关业务方法
 */
public interface PostUserSignService {
    /**
     * 发送登录/注册验证码
     * 向指定邮箱发送验证码，用于用户登录或注册验证
     * 
     * @param mail 用户邮箱地址
     * @return boolean 返回验证码发送结果，true表示成功，false表示失败
     */
    boolean sendSignVerificationCode(String mail);

    /**
     * 验证登录/注册验证码
     * 验证用户提交的邮箱验证码，根据验证结果和用户注册状态返回不同的处理结果
     * 
     * @param request 包含邮箱和验证码的请求对象
     * @return Pair<PostUserSignEnum, Object> 返回验证结果和附加信息
     *         - SUCCESS_LOGIN: 验证成功且用户已注册，返回token
     *         - SUCCESS_BUT_NOT_REGISTERED: 验证成功但用户未注册，返回临时UUID用于后续注册
     *         - INVALID_VERIFICATION_CODE: 验证失败，验证码错误或已过期
     */
    Pair<PostUserSignEnum, Object> verifySignVerificationCode(VerifySignVerificationCodeRequest request);

    /**
     * 用户注册
     * 根据拟注册的uid和用户信息完成注册
     * 
     * @param request 注册请求，包含用户信息和拟注册的uid
     * @return boolean 返回注册结果，true表示成功，false表示失败
     */
    Pair<PostUserSignEnum, Object> register(RegisterRequest request);
}
