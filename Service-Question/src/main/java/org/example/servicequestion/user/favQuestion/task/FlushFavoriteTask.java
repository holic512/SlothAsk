/**
 * File Name: FlushFavoriteTask.java
 * Description: 定时任务，批量处理收藏消息
 * Author: holic512
 * Created Date: 2025-04-28
 * Version: 1.0
 */
package org.example.servicequestion.user.favQuestion.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicequestion.user.favQuestion.consumer.MessageBuffer;
import org.example.servicequestion.user.favQuestion.dto.FavoriteMessage;
import org.example.servicequestion.user.favQuestion.service.PostFavQuestionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时刷库任务，每隔一定时间检查是否有需要处理的收藏消息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlushFavoriteTask {

    private static final int BATCH_SIZE = 100;       // 批量大小阈值
    private static final long TIMEOUT_MS = 30000;     // 超时时间，毫秒
    private final MessageBuffer messageBuffer;
    private final PostFavQuestionService postFavQuestionService;

    @Scheduled(fixedDelay = 1000) // 每1秒执行一次
    public void flushFavorites() {
        try {
            List<FavoriteMessage> messages = messageBuffer.flushIfNeeded(BATCH_SIZE, TIMEOUT_MS);
            if (!messages.isEmpty()) {
                log.info("开始批量处理收藏消息，数量: {}", messages.size());
                postFavQuestionService.batchSave(messages);

                messageBuffer.basicAck();
                log.info("收藏消息处理完成");
            }
        } catch (Exception e) {
            log.error("批量处理收藏消息失败", e);
        }
    }
}
