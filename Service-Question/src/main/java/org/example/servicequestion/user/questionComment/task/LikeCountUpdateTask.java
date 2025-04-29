/**
 * File Name: LikeCountUpdateTask.java
 * Description: 评论点赞数更新定时任务
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LikeCountUpdateTask {

    private static final Logger logger = LoggerFactory.getLogger(LikeCountUpdateTask.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 每1分钟更新一次评论点赞数
     */
    @Scheduled(fixedDelay = 60000) // 60s
    public void updateCommentLikeCount() {
        try {
            logger.info("开始更新评论点赞数...");
            
            // 使用SQL直接更新评论表的点赞数
            String sql = "UPDATE question_comment qc SET like_count = (" +
                    "SELECT COUNT(*) FROM question_comment_like qcl " +
                    "WHERE qcl.comment_id = qc.id)";
            
            int updatedRows = jdbcTemplate.update(sql);
            
            logger.info("评论点赞数更新完成，共更新 {} 条记录", updatedRows);
        } catch (Exception e) {
            logger.error("更新评论点赞数时出错: {}", e.getMessage(), e);
        }
    }
} 