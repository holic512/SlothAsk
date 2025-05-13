/**
 * File Name: CategoryViewCountUpdateTask.java
 * Description: 题库分类访问量更新定时任务
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.question.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CategoryViewCountUpdateTask {

    private static final Logger logger = LoggerFactory.getLogger(CategoryViewCountUpdateTask.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 每15分钟更新一次题库分类的访问量
     */
    @Scheduled(fixedDelay = 900000) // 15分钟 = 900000毫秒
    public void updateCategoryViewCount() {
        try {
            logger.info("开始更新题库分类访问量...");
            
            // 使用SQL直接更新题库分类表的访问量，将其设置为该分类下所有问题的访问量总和
            String sql = "UPDATE question_category qc SET view_count = (" +
                    "SELECT COALESCE(SUM(view_count), 0) FROM question q " +
                    "WHERE q.category_id = qc.id)";
            
            int updatedRows = jdbcTemplate.update(sql);
            
            logger.info("题库分类访问量更新完成，共更新 {} 条记录", updatedRows);
        } catch (Exception e) {
            logger.error("更新题库分类访问量时出错: {}", e.getMessage(), e);
        }
    }
} 