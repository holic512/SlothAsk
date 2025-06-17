/**
 * File Name: UserProfileServiceImpl.java
 * Description: 用户个人资料服务实现类，实现与用户问题相关的信息查询功能
 * Author: holic512
 * Created Date: 2025-06-16
 * Version: 1.0
 * Usage:
 * 实现UserProfileService接口，提供问题标题、标签列表和访问量查询功能，并使用Redis缓存优化标签查询性能
 */
package org.example.servicequestion.user.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionTagCategory;
import org.example.servicequestion.user.user.mapper.UserUserQuestionQuestionMapper;
import org.example.servicequestion.user.user.mapper.UserUserQuestionTagMapper;
import org.example.servicequestion.user.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final static String TAG_CACHE_KEY = RedisConfig.getKey() + "Tag:Detail:";
    private final UserUserQuestionQuestionMapper questionMapper;
    private final UserUserQuestionTagMapper tagMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserProfileServiceImpl(UserUserQuestionQuestionMapper questionMapper, UserUserQuestionTagMapper tagMapper, RedisTemplate<String, Object> redisTemplate) {
        this.questionMapper = questionMapper;
        this.tagMapper = tagMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getQuestionTitle(Long questionId) {
        Question question = questionMapper.selectOne(
                new LambdaQueryWrapper<Question>()
                        .eq(Question::getId, questionId)
                        .select(Question::getTitle)
        );
        return question != null ? question.getTitle() : null;
    }

    /**
     * 根据问题ID获取标签名称列表
     *
     * @param questionId 问题ID
     * @return 标签名称列表
     */
    public List<String> getTagNamesByQuestionId(Long questionId) {
        Question question = questionMapper.selectById(questionId);
        if (question == null || question.getTagCategoryIds() == null || question.getTagCategoryIds().isEmpty()) {
            return new ArrayList<>();
        }
        List<String> tagNames = new ArrayList<>();
        List<Long> missingTagIds = new ArrayList<>();
        for (Integer tagIdInt : question.getTagCategoryIds()) {
            Long tagId = tagIdInt.longValue();
            String cacheKey = TAG_CACHE_KEY + tagId;
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof QuestionTagCategory) {
                tagNames.add(((QuestionTagCategory) cached).getName());
            } else if (cached instanceof java.util.Map) {
                Object name = ((java.util.Map<?, ?>) cached).get("name");
                if (name instanceof String) {
                    tagNames.add((String) name);
                } else {
                    missingTagIds.add(tagId);
                }
            } else {
                missingTagIds.add(tagId);
            }
        }
        if (!missingTagIds.isEmpty()) {
            List<QuestionTagCategory> dbTags = tagMapper.selectBatchIdsSimple(missingTagIds);
            for (QuestionTagCategory tag : dbTags) {
                String cacheKey = TAG_CACHE_KEY + tag.getId();
                redisTemplate.opsForValue().set(cacheKey, tag, 1, TimeUnit.DAYS);
                tagNames.add(tag.getName());
            }
        }
        return tagNames;
    }

    @Override
    public Long getQuestionViewCount(Long questionId) {
        Question question = questionMapper.selectOne(
                new LambdaQueryWrapper<Question>()
                        .eq(Question::getId, questionId)
                        .select(Question::getViewCount)
        );
        return question != null ? question.getViewCount() : 0L;
    }
}
