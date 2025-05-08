/**
 * File Name: GitHubAuthServiceImpl.java
 * Description: GitHub OAuth认证服务实现类
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * 处理GitHub OAuth认证流程，包括获取访问令牌和用户信息
 */
package org.example.serviceuser.user.sign.service.Impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserAuth;
import org.example.serviceuser.entity.UserProfile;
import org.example.serviceuser.entity.UserProjectCategory;
import org.example.serviceuser.user.sign.dto.GitHubUserDTO;
import org.example.serviceuser.user.sign.enums.AuthProvider;
import org.example.serviceuser.user.sign.enums.PostUserSignEnum;
import org.example.serviceuser.user.sign.exception.GitHubAuthException;
import org.example.serviceuser.user.sign.mapper.UserAuthMapper;
import org.example.serviceuser.user.sign.mapper.UserSignProfileMapper;
import org.example.serviceuser.user.sign.mapper.UserSignUpcMapper;
import org.example.serviceuser.user.sign.mapper.UserSignUserMapper;
import org.example.serviceuser.user.sign.service.GitHubAuthService;
import org.example.serviceuser.util.NicknameGenerator;
import org.example.serviceuser.util.StpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * GitHub OAuth认证服务实现类
 * 使用OkHttp客户端直接发送HTTP请求，解决连接超时问题
 */
@Slf4j
@Service
public class GitHubAuthServiceImpl implements GitHubAuthService {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    
    // 区分用户错误和系统错误的常量
    private static final String ERROR_CODE_EXPIRED = "The code passed is incorrect or expired";
    private static final String ERROR_BAD_VERIFICATION_CODE = "bad_verification_code";
    private static final String ERROR_EMPTY_TOKEN = "access_token";
    // 直接使用OkHttpClient
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    // 额外注入需要的依赖
    private final UserAuthMapper userAuthMapper;
    private final UserSignUserMapper userSignUserMapper;
    private final UserSignProfileMapper userSignProfileMapper;
    private final UserSignUpcMapper userSignUpcMapper;
    @Value("${github.client-id}")
    private String clientId;
    @Value("${github.client-secret}")
    private String clientSecret;
    @Value("${github.redirect-uri}")
    private String redirectUri;

    /**
     * 构造函数，通过依赖注入获取所需服务和组件
     *
     * @param okHttpClient OkHttp客户端
     * @param objectMapper JSON序列化/反序列化组件
     */
    @Autowired
    public GitHubAuthServiceImpl(OkHttpClient okHttpClient, 
                                ObjectMapper objectMapper,
                                UserAuthMapper userAuthMapper,
                                UserSignUserMapper userSignUserMapper,
                                UserSignProfileMapper userSignProfileMapper,
                                UserSignUpcMapper userSignUpcMapper) {
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
        this.userAuthMapper = userAuthMapper;
        this.userSignUserMapper = userSignUserMapper;
        this.userSignProfileMapper = userSignProfileMapper;
        this.userSignUpcMapper = userSignUpcMapper;
        log.info("初始化GitHubAuthServiceImpl，使用OkHttp客户端");
    }

    /**
     * 获取GitHub访问令牌
     *
     * @param code GitHub授权码
     * @param state 状态参数，用于防止CSRF攻击
     * @return GitHub访问令牌
     * @throws GitHubAuthException 如果发生认证相关错误
     */
    @Override
    @Retryable(
            value = {IOException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String getAccessToken(String code, String state) throws GitHubAuthException {
        // 参数校验
        if (StringUtils.isBlank(code)) {
            log.warn("GitHub授权码为空");
            throw new GitHubAuthException("GitHub授权码不能为空", true);
        }

        String tokenUrl = "https://github.com/login/oauth/access_token";

        try {
            log.info("开始使用OkHttp请求GitHub获取访问令牌，授权码：{}", code);

            // 构建请求体
            String requestBody = String.format("""
                    {
                      "client_id": "%s",
                      "client_secret": "%s",
                      "code": "%s",
                      "redirect_uri": "%s",
                      "state": "%s"
                    }
                """, clientId, clientSecret, code, redirectUri, state);

            // 构建请求
            Request request = new Request.Builder()
                    .url(tokenUrl)
                    .header("Accept", "application/json")
                    .post(RequestBody.create(requestBody, JSON_MEDIA_TYPE))
                    .build();

            // 执行请求
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    log.error("GitHub API响应错误，状态码: {}", response.code());
                    throw new GitHubAuthException("GitHub API返回错误，状态码: " + response.code(), false);
                }

                // 解析响应
                String responseBody = response.body().string();
                log.debug("GitHub响应: {}", responseBody);

                JsonNode json = objectMapper.readTree(responseBody);

                // 检查GitHub返回的错误信息
                if (json.has("error")) {
                    String errorType = json.get("error").asText();
                    String errorMessage = json.get("error_description").asText();
                    
                    // 判断是否是用户错误（如授权码过期）
                    if (ERROR_BAD_VERIFICATION_CODE.equals(errorType) || 
                            (errorMessage != null && errorMessage.contains(ERROR_CODE_EXPIRED))) {
                        log.warn("GitHub OAuth用户错误: {}. 错误描述: {}", errorType, errorMessage);
                        throw new GitHubAuthException("GitHub认证失败: " + errorMessage, true);
                    } else {
                        log.error("GitHub OAuth系统错误: {}. 错误描述: {}", errorType, errorMessage);
                        throw new GitHubAuthException("GitHub认证失败: " + errorMessage, false);
                    }
                }

                // 检查access_token字段是否存在
                if (!json.has("access_token")) {
                    log.warn("GitHub响应中未包含access_token字段");
                    throw new GitHubAuthException("无法获取GitHub访问令牌", true);
                }

                String accessToken = json.get("access_token").asText();
                log.info("成功获取GitHub访问令牌");
                return accessToken;
            }
        } catch (IOException e) {
            log.error("GitHub API连接异常: {}", e.getMessage(), e);
            throw new GitHubAuthException("连接GitHub服务器异常，请稍后重试", e, false);
        } catch (GitHubAuthException e) {
            // 直接抛出，不添加额外的堆栈信息
            throw e;
        } catch (Exception e) {
            log.error("获取GitHub访问令牌时发生未知异常: {}", e.getMessage(), e);
            throw new GitHubAuthException("GitHub认证过程中发生未知错误", e, false);
        }
    }

    /**
     * 获取GitHub用户信息
     *
     * @param accessToken GitHub访问令牌
     * @return GitHub用户信息
     * @throws GitHubAuthException 如果获取用户信息失败
     */
    @Override
    @Retryable(
            value = {IOException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public GitHubUserDTO getUserInfo(String accessToken) throws GitHubAuthException {
        // 参数校验
        if (StringUtils.isBlank(accessToken)) {
            log.warn("GitHub访问令牌为空");
            throw new GitHubAuthException("GitHub访问令牌不能为空", true);
        }

        String userUrl = "https://api.github.com/user";

        try {
            log.info("开始使用OkHttp请求GitHub获取用户信息");

            // 构建请求
            Request request = new Request.Builder()
                    .url(userUrl)
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            // 执行请求，设置10秒超时
            OkHttpClient clientWithTimeout = okHttpClient.newBuilder()
                    .callTimeout(10, TimeUnit.SECONDS)
                    .build();

            try (Response response = clientWithTimeout.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    log.error("获取GitHub用户信息失败，状态码: {}", response.code());
                    throw new GitHubAuthException("获取GitHub用户信息失败，状态码: " + response.code(), false);
                }

                // 解析响应
                String responseBody = response.body().string();
                log.debug("GitHub用户信息响应: {}", responseBody);

                GitHubUserDTO userDTO = objectMapper.readValue(responseBody, GitHubUserDTO.class);
                log.info("成功获取GitHub用户信息: {}", Optional.ofNullable(userDTO.getLogin()).orElse("未知用户"));
                return userDTO;
            }
        } catch (IOException e) {
            log.error("获取GitHub用户信息连接异常: {}", e.getMessage(), e);
            throw new GitHubAuthException("连接GitHub服务器异常，请稍后重试", e, false);
        } catch (GitHubAuthException e) {
            // 直接抛出，不添加额外的堆栈信息
            throw e;
        } catch (Exception e) {
            log.error("获取GitHub用户信息时发生未知异常: {}", e.getMessage(), e);
            throw new GitHubAuthException("获取GitHub用户信息过程中发生未知错误", e, false);
        }
    }

    /**
     * 处理GitHub用户登录
     * 如果GitHub用户未绑定系统用户，则自动创建新用户
     * 如果已绑定，则直接登录该用户
     *
     * @param githubUser GitHub用户信息
     * @return 处理结果和登录令牌
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<PostUserSignEnum, Object> handleGitHubLogin(GitHubUserDTO githubUser) {
        if (githubUser == null || githubUser.getId() == null) {
            log.warn("GitHub用户信息为空");
            throw new GitHubAuthException("无效的GitHub用户信息", true);
        }
        
        String provider = AuthProvider.GITHUB.getValue();
        String openId = githubUser.getId().toString(); // GitHub用户ID作为openId
        
        try {
            // 检查该GitHub账号是否已经绑定了系统用户
            UserAuth userAuth = userAuthMapper.findByProviderAndOpenId(provider, openId);
            
            User user;
            if (userAuth == null) {
                // 如果未绑定，创建新用户
                log.info("GitHub用户{}首次登录，创建新用户", githubUser.getLogin());
                user = createUserWithGitHub(githubUser);
            } else {
                // 如果已绑定，获取对应的用户
                log.info("GitHub用户{}已绑定系统用户，直接登录", githubUser.getLogin());
                user = userSignUserMapper.selectById(userAuth.getUserId());
                
                if (user == null) {
                    log.error("绑定的用户不存在，用户ID: {}", userAuth.getUserId());
                    throw new GitHubAuthException("绑定的用户账号不存在", false);
                }
            }
            
            // 登录用户
            StpKit.USER.login(user.getId());
            SaTokenInfo saTokenInfo = StpKit.USER.getTokenInfo();
            
            // 获取或设置用户项目类别ID
            Long userUpcId = userSignUpcMapper.selectPidByUid(user.getId());
            
            // 如果用户项目id为空 则设置为 默认 1
            if (userUpcId == null) {
                UserProjectCategory userPc = new UserProjectCategory();
                userPc.setUserId(user.getId());
                userPc.setProjectId(1L);
                userSignUpcMapper.insert(userPc);
                
                userUpcId = 1L;
            }
            
            SaSession session = StpKit.USER.getSession();
            session.set("userUpcId", userUpcId);
            
            // 返回token
            return Pair.of(PostUserSignEnum.SUCCESS_LOGIN, saTokenInfo);
            
        } catch (GitHubAuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("处理GitHub登录过程中发生异常: {}", e.getMessage(), e);
            throw new GitHubAuthException("登录过程中发生错误", e, false);
        }
    }
    
    /**
     * 创建新用户并绑定GitHub账号
     *
     * @param githubUser GitHub用户信息
     * @return 创建的用户
     */
    private User createUserWithGitHub(GitHubUserDTO githubUser) {
        // 1. 创建用户基本信息
        User user = new User();
        // 使用GitHub名称作为起始，生成随机用户名
        String baseUsername = githubUser.getLogin() != null ? githubUser.getLogin() : "github";
        String username = baseUsername;
        
        // 检查用户名是否存在，如果存在则添加随机数后缀
        User existingUser = userSignUserMapper.getUserByUsername(username);
        if (existingUser != null) {
            username = baseUsername + "_" + (int)(Math.random() * 10000);
        }
        
        user.setUsername(username);
        user.setEmail(githubUser.getEmail()); // GitHub可能不返回邮箱
        user.setPassword(""); // GitHub登录不需要密码
        
        // 保存用户信息
        userSignUserMapper.insert(user);
        
        // 2. 创建用户个人资料
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getId());
        
        // 使用GitHub名称作为昵称，如果为空则生成随机昵称
        String nickname = githubUser.getName();
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = githubUser.getLogin();
        }
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = NicknameGenerator.generateNickname();
        }
        
        userProfile.setNickname(nickname);
        userProfile.setAvatar(githubUser.getAvatarUrl()); // 使用GitHub头像
        userProfile.setBio(githubUser.getBio()); // 使用GitHub简介
        
        // 保存用户资料
        userSignProfileMapper.insert(userProfile);
        
        // 3. 创建第三方认证绑定记录
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(user.getId());
        userAuth.setProvider(AuthProvider.GITHUB.getValue());
        userAuth.setOpenId(githubUser.getId().toString());
        userAuth.setStatus(1); // 正常状态
        userAuth.setIsDeleted(0); // 未删除
        
        // 保存认证绑定信息
        userAuthMapper.insert(userAuth);
        
        // 4. 设置默认项目类别
        UserProjectCategory userPc = new UserProjectCategory();
        userPc.setUserId(user.getId());
        userPc.setProjectId(1L);
        userSignUpcMapper.insert(userPc);
        
        return user;
    }
}
