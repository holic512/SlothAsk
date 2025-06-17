/**
 * File Name: GetUserProfileServiceImpl.java
 * Description: 用户个人资料服务实现类，提供用户信息、评论和收藏问题的查询功能
 * Author: holic512
 * Created Date: 2025-06-12
 * Version: 1.0
 * Usage:
 * 实现GetUserProfileService接口，提供获取用户基本信息、评论列表和收藏问题列表的功能，并使用Redis缓存优化查询性能
 */
package org.example.serviceuser.user.profile.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.serviceuser.common.UserAvatarService;
import org.example.serviceuser.config.Redis.RedisConstants;
import org.example.serviceuser.entity.QuestionComment;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserFavoriteQuestion;
import org.example.serviceuser.entity.UserProfile;
import org.example.serviceuser.feign.QuestionFeign;
import org.example.serviceuser.user.info.mapper.UserInfoUserMapper;
import org.example.serviceuser.user.info.mapper.UserInfoUserProfileMapper;
import org.example.serviceuser.user.profile.dto.CommentDto;
import org.example.serviceuser.user.profile.dto.FavQuestionItemDto;
import org.example.serviceuser.user.profile.dto.UserProfileInfoDto;
import org.example.serviceuser.user.profile.mapper.UserProfileFavoriteQuestionMapper;
import org.example.serviceuser.user.profile.mapper.UserProfileQuestionCommentMapper;
import org.example.serviceuser.user.profile.service.GetUserProfileService;
import org.example.serviceuser.util.CodeAgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetUserProfileServiceImpl implements GetUserProfileService {

    private final UserInfoUserMapper userInfoUserMapper;
    private final UserInfoUserProfileMapper userInfoUserProfileMapper;
    private final UserAvatarService userAvatarService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserProfileQuestionCommentMapper questionCommentMapper;
    private final ObjectMapper objectMapper;
    private final QuestionFeign questionFeign;
    private final UserProfileFavoriteQuestionMapper favoriteQuestionMapper;

    @Autowired
    public GetUserProfileServiceImpl(UserInfoUserMapper userInfoUserMapper, UserInfoUserProfileMapper userInfoUserProfileMapper,
                                     UserAvatarService userAvatarService, RedisTemplate<String, String> redisTemplate,
                                     UserProfileQuestionCommentMapper questionCommentMapper, ObjectMapper objectMapper,
                                     QuestionFeign questionFeign, UserProfileFavoriteQuestionMapper favoriteQuestionMapper) {
        this.userInfoUserMapper = userInfoUserMapper;
        this.userInfoUserProfileMapper = userInfoUserProfileMapper;
        this.userAvatarService = userAvatarService;
        this.redisTemplate = redisTemplate;
        this.questionCommentMapper = questionCommentMapper;
        this.objectMapper = objectMapper;
        this.questionFeign = questionFeign;
        this.favoriteQuestionMapper = favoriteQuestionMapper;
    }

    @Override
    public UserProfileInfoDto getUserProfileInfo(String username, Long currentUserId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .select(User::getId);

        User user = userInfoUserMapper.selectOne(queryWrapper);
        Long userId;
        if (user == null) return null;
        else userId = user.getId();

        // 创建dto
        UserProfileInfoDto userProfileInfoDto = new UserProfileInfoDto();

        // 判断是否是 当前是本人访问
        userProfileInfoDto.setIsSelf(userId.equals(currentUserId));

        // 查询用户的基础信息 包含头像(处理),昵称,年龄,性别,居住地,注册时间,生日,个人简介
        LambdaQueryWrapper<UserProfile> profileLambdaQueryWrapper = new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId)
                .select(
                        UserProfile::getAvatar,
                        UserProfile::getNickname,
                        UserProfile::getAge,
                        UserProfile::getGender,
                        UserProfile::getLocation,
                        UserProfile::getCreateTime,
                        UserProfile::getBirthday,
                        UserProfile::getBio
                );
        UserProfile userProfile = userInfoUserProfileMapper.selectOne(profileLambdaQueryWrapper);

        // 插入基础数据 除了头像 需要处理
        userProfileInfoDto.setNickname(userProfile.getNickname());
        userProfileInfoDto.setAge(userProfile.getAge());
        userProfileInfoDto.setGender(userProfile.getGender());
        userProfileInfoDto.setProvince(userProfile.getLocation());
        userProfileInfoDto.setJoinDate(LocalDate.from(userProfile.getCreateTime()));
        userProfileInfoDto.setBirthday(userProfile.getBirthday());
        userProfileInfoDto.setBio(userProfile.getBio());


        // 获取头像的真实URL/当用户的头像存储在系统库的时候
        String realAvatarUrl = userAvatarService.getUserAvatar(userProfile.getAvatar());
        userProfileInfoDto.setAvatar(realAvatarUrl);

        // 处理用户的码龄
        userProfileInfoDto.setCodingAge(CodeAgeUtil.calculateCodeAgeStr(userProfile.getCreateTime()));

        // todo 代做获取用户关注 以及是否关注用户

        return userProfileInfoDto;
    }

    @Override
    public Page<CommentDto> getCommentsByUsername(String username, int page, int size) {
        // 1. 根据用户名查询用户ID
        User user = userInfoUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );
        if (user == null) {
            return new Page<>();
        }

        // 2. 构建缓存key
        String cacheKey = RedisConstants.Comment.USER_COMMENTS_PREFIX + user.getId() + ":" + page + ":" + size;

        // 3. 尝试从缓存获取
        String cachedResult = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            try {
                return objectMapper.readValue(cachedResult, new TypeReference<Page<CommentDto>>() {
                });
            } catch (Exception e) {
                // 如果反序列化失败，继续执行数据库查询
            }
        }

        // 4. 缓存未命中，查询数据库
        Page<QuestionComment> commentPage = questionCommentMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<QuestionComment>()
                        .eq(QuestionComment::getUserId, user.getId())
                        .orderByDesc(QuestionComment::getCreateTime)
        );

        // 5. 转换为DTO
        Page<CommentDto> dtoPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        List<CommentDto> dtoList = commentPage.getRecords().stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setContent(comment.getContent());
                    dto.setLikes(comment.getLikeCount());
                    dto.setCreatedAt(comment.getCreateTime().toString());

                    // 获取问题信息
                    try {
                        Map<String, String> questionInfo = questionFeign.getQuestionUidAndTitle(comment.getQuestionId());
                        dto.setQuestionTUid(questionInfo.get("questionTUid"));
                        dto.setQuestionTitle(questionInfo.get("questionTitle"));
                    } catch (Exception e) {
                        // 如果获取问题信息失败，记录日志但继续执行
                    }

                    return dto;
                })
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);

        // 6. 写入缓存
        try {
            redisTemplate.opsForValue().set(
                    cacheKey,
                    objectMapper.writeValueAsString(dtoPage),
                    RedisConstants.Comment.getUserCommentsTtlWithJitter()
            );
        } catch (Exception e) {
            // 如果序列化失败，记录日志但继续执行
        }

        return dtoPage;
    }

    @Override
    public Page<FavQuestionItemDto> getFavQuestionsByUsername(String username, int page, int size) {
        // 1. 根据用户名查询用户ID
        User user = userInfoUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );
        if (user == null) {
            return new Page<>();
        }

        // 2. 构建缓存key
        String cacheKey = RedisConstants.Favorite.USER_FAVORITES_PREFIX + user.getId() + ":" + page + ":" + size;

        // 3. 尝试从缓存获取
        String cachedResult = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            try {
                return objectMapper.readValue(cachedResult, new TypeReference<Page<FavQuestionItemDto>>() {
                });
            } catch (Exception e) {
                // 如果反序列化失败，继续执行数据库查询
            }
        }

        // 4. 缓存未命中，查询数据库
        LambdaQueryWrapper<UserFavoriteQuestion> wrapper = new LambdaQueryWrapper<UserFavoriteQuestion>()
                .eq(UserFavoriteQuestion::getUserId, user.getId())
                .orderByDesc(UserFavoriteQuestion::getCreateTime);

        Page<UserFavoriteQuestion> favoriteQuestionPage = favoriteQuestionMapper.selectPage(
                new Page<>(page, size), wrapper);

        // 5. 转换为DTO
        Page<FavQuestionItemDto> dtoPage = new Page<>(favoriteQuestionPage.getCurrent(), favoriteQuestionPage.getSize(), favoriteQuestionPage.getTotal());
        List<FavQuestionItemDto> dtoList = favoriteQuestionPage.getRecords().stream()
                .map(favorite -> {
                    FavQuestionItemDto dto = new FavQuestionItemDto();
                    dto.setSavedAt(favorite.getCreateTime().toString());

                    // 获取问题信息
                    try {
                        Map<String, Object> questionInfo = questionFeign.getQuestionUidAndTitleAndTags(favorite.getQuestionId());
                        dto.setQuestionId((String) questionInfo.get("questionTUid"));
                        dto.setQuestionTitle((String) questionInfo.get("questionTitle"));
                        dto.setTags((List<String>) questionInfo.get("tags"));
                        
                        // 处理访问量，可能是Integer或Long
                        Object viewCountObj = questionInfo.get("viewCount");
                        if (viewCountObj instanceof Long) {
                            dto.setViews(((Long) viewCountObj).intValue());
                        } else if (viewCountObj instanceof Integer) {
                            dto.setViews((Integer) viewCountObj);
                        } else {
                            dto.setViews(0);
                        }
                    } catch (Exception e) {
                        // 如果获取问题信息失败，记录日志但继续执行
                        dto.setQuestionId("unknown");
                        dto.setQuestionTitle("问题信息获取失败");
                        dto.setTags(new ArrayList<>());
                        dto.setViews(0);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);

        // 6. 写入缓存
        try {
            redisTemplate.opsForValue().set(
                    cacheKey,
                    objectMapper.writeValueAsString(dtoPage),
                    RedisConstants.Favorite.getUserFavoritesTtlWithJitter()
            );
        } catch (Exception e) {
            // 如果序列化失败，记录日志但继续执行
        }

        return dtoPage;
    }
}
