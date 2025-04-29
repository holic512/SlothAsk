/**
 * File Name: PostFavQuestionService.java
 * Description: 用户收藏问题服务接口，定义了添加、移除收藏题目以及批量保存收藏记录的功能方法。
 * Author: holic512
 * Created Date: 2025-04-23
 * Version: 1.0
 * Usage:
 * 提供了用户收藏问题操作的接口，具体实现由 PostFavQuestionServiceImpl 类完成，支持向 Redis、数据库保存数据，并通过消息队列实现异步操作。
 */

package org.example.servicequestion.user.favQuestion.service;

import org.example.servicequestion.user.favQuestion.dto.FavoriteMessage;
import org.example.servicequestion.user.favQuestion.enums.PostFavQuestionEnum;

import java.util.List;

public interface PostFavQuestionService {

    PostFavQuestionEnum addFavQuestion(String virtualId, Long userId);

    PostFavQuestionEnum removeFavQuestion(String virtualId, Long userId);

    // 批量插入 给消费者使用的
    void batchSave(List<FavoriteMessage> messages);
}
