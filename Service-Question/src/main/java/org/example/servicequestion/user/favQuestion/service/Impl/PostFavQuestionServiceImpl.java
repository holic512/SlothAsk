/**
 * File Name: PostFavQuestionServiceImpl.java
 * Description: 用户收藏问题服务实现类，提供添加、移除收藏题目以及批量保存收藏记录的功能。
 * Author: holic512
 * Created Date: 2025-04-23
 * Version: 1.0
 * Usage:
 * 实现了 PostFavQuestionService 接口，提供用户收藏题目的增删操作，支持将收藏信息保存到 Redis 和数据库，并通过消息队列处理异步操作。
 */

package org.example.servicequestion.user.favQuestion.service.Impl;

import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.config.Redis.service.RedisZSetService;
import org.example.servicequestion.config.rabbit.FavRabbitConfig;
import org.example.servicequestion.entity.UserFavoriteQuestion;
import org.example.servicequestion.user.commonService.IdConversionService;
import org.example.servicequestion.user.favQuestion.dto.FavoriteMessage;
import org.example.servicequestion.user.favQuestion.enums.PostFavQuestionEnum;
import org.example.servicequestion.user.favQuestion.mapper.FavQUserFavoriteQuestionMapper;
import org.example.servicequestion.user.favQuestion.service.PostFavQuestionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostFavQuestionServiceImpl implements PostFavQuestionService {

    private final IdConversionService idConversionService;
    private final RedisZSetService redisZSetService;
    private final FavRabbitConfig favRabbitConfig;
    private final RabbitTemplate rabbitTemplate;
    private final FavQUserFavoriteQuestionMapper favQUserFavoriteQuestionMapper;


    @Autowired
    public PostFavQuestionServiceImpl(IdConversionService idConversionService,
                                      RedisZSetService redisZSetService,
                                      FavRabbitConfig favRabbitConfig,
                                      RabbitTemplate rabbitTemplate,
                                      FavQUserFavoriteQuestionMapper favQUserFavoriteQuestionMapper) {
        this.idConversionService = idConversionService;
        this.redisZSetService = redisZSetService;
        this.favRabbitConfig = favRabbitConfig;
        this.rabbitTemplate = rabbitTemplate;
        this.favQUserFavoriteQuestionMapper = favQUserFavoriteQuestionMapper;
    }

    @Override
    public PostFavQuestionEnum addFavQuestion(String virtualId, Long userId) {
        try {
            // 参数校验
            if (virtualId == null || userId == null || virtualId.isEmpty())
                return PostFavQuestionEnum.FAIL;

            // 1.解析问题id
            Long realQuestionId = idConversionService.getOriginalIdFromVirtualId(virtualId);
            if (realQuestionId == null) return PostFavQuestionEnum.FAIL;

            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
            long shanghaiTimestamp = zdt.toInstant().toEpochMilli();

            // 2.将当前数据保存至Redis中的ZSet中(分数作为时间戳)
            redisZSetService.addToZSetWithExpire(RedisKey.FAV_QUESTION_USER_KEY + userId, realQuestionId, shanghaiTimestamp, 600);


            // 3. 将数据插入到消息队列中
            FavoriteMessage favoriteMessage = new FavoriteMessage(userId, realQuestionId, "fav", shanghaiTimestamp);
            favRabbitConfig.sendMessage(rabbitTemplate, favoriteMessage);

            return PostFavQuestionEnum.SUCCESS;
        } catch (Exception e) {
            return PostFavQuestionEnum.FAIL;
        }

    }

    @Override
    public PostFavQuestionEnum removeFavQuestion(String virtualId, Long userId) {
        try {
            // 参数校验
            if (virtualId == null || userId == null || virtualId.isEmpty())
                return PostFavQuestionEnum.FAIL;

            // 1.解析问题id
            Long realQuestionId = idConversionService.getOriginalIdFromVirtualId(virtualId);
            if (realQuestionId == null) return PostFavQuestionEnum.FAIL;

            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
            long shanghaiTimestamp = zdt.toInstant().toEpochMilli();

            // 2.将当前数据移除至Redis中的ZSet中(分数作为时间戳)
            redisZSetService.removeFromZSet(RedisKey.FAV_QUESTION_USER_KEY + userId, realQuestionId);


            // 3. 将数据插入到消息队列中
            FavoriteMessage favoriteMessage = new FavoriteMessage(userId, realQuestionId, "unfav", shanghaiTimestamp);
            favRabbitConfig.sendMessage(rabbitTemplate, favoriteMessage);

            return PostFavQuestionEnum.SUCCESS;
        } catch (Exception e) {
            return PostFavQuestionEnum.FAIL;
        }
    }


    @Override
    public void batchSave(List<FavoriteMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        // 用 Map 去重，key = userId + "_" + questionId，value = 最新的 FavoriteMessage
        Map<String, FavoriteMessage> latestActionMap = new HashMap<>();

        for (FavoriteMessage message : messages) {
            String key = message.getUserId() + "_" + message.getQuestionId();
            FavoriteMessage existing = latestActionMap.get(key);

            if (existing == null || message.getTimestamp() > existing.getTimestamp()) {
                latestActionMap.put(key, message);
            }
        }

        List<UserFavoriteQuestion> toInsert = new ArrayList<>();
        List<UserFavoriteQuestion> toDelete = new ArrayList<>();

        for (FavoriteMessage message : latestActionMap.values()) {
            if ("fav".equalsIgnoreCase(message.getAction())) {
                UserFavoriteQuestion userFavoriteQuestion = new UserFavoriteQuestion();
                userFavoriteQuestion.setQuestionId(message.getQuestionId());
                userFavoriteQuestion.setUserId(message.getUserId());

                // 解析时间戳并设置时区
                LocalDateTime localDateTime = Instant.ofEpochMilli(message.getTimestamp())
                        .atZone(ZoneId.of("Asia/Shanghai"))
                        .toLocalDateTime();

                userFavoriteQuestion.setCreateTime(localDateTime);

                toInsert.add(userFavoriteQuestion);
            } else if ("unfav".equalsIgnoreCase(message.getAction())) {
                UserFavoriteQuestion userFavoriteQuestion = new UserFavoriteQuestion();
                userFavoriteQuestion.setQuestionId(message.getQuestionId());
                userFavoriteQuestion.setUserId(message.getUserId());
                toDelete.add(userFavoriteQuestion);
            }
        }

        if (!toInsert.isEmpty()) {
            favQUserFavoriteQuestionMapper.batchInsertOrUpdate(toInsert);
        }
        if (!toDelete.isEmpty()) {
            favQUserFavoriteQuestionMapper.batchDeleteByUserAndQuestion(toDelete);
        }
    }
}
