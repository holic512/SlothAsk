/**
 * File Name: GetUserCategoryServiceImpl.java
 * Description: 获取用户题库分类服务实现类
 * Author: holic512
 * Created Date: 2025-03-19
 * Version: 1.0
 * Usage:
 * 提供用户题库分类相关的查询服务实现
 */
package org.example.servicequestion.user.category.service.Impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.user.category.mapper.QuestionCategoryMapper;
import org.example.servicequestion.user.category.service.GetUserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GetUserCategoryServiceImpl implements GetUserCategoryService {

    @Autowired
    private QuestionCategoryMapper questionCategoryMapper;

    @Autowired
    private ServiceImageFeign serviceImageFeign;
    
    // 本地缓存，用于存储文件名到URL的映射，避免重复调用Feign服务
    private final Map<String, String> urlCache = new ConcurrentHashMap<>();

    @Override
    @Cacheable(value = "recommendedCategories", key = "#projectId != null ? #projectId : 'all'", unless = "#result == null || #result.isEmpty()")
    public List<QuestionCategory> getRecommendedCategories(Long projectId) {

        List<QuestionCategory> result;

        if (projectId != null) {
            // 当存在项目ID时，限定条件为project_id
            result = questionCategoryMapper.findRecommendedCategoriesByProjectId(projectId);
        } else {
            // 不存在项目ID时，从全部分类中获取
            result = questionCategoryMapper.findRecommendedCategories();
        }

        // 修改图片地址 获取真实的 url
        if (result != null && !result.isEmpty()) {
            // 收集所有需要处理的文件名
            Map<QuestionCategory, String> categoriesToProcess = new ConcurrentHashMap<>();
            
            for (QuestionCategory category : result) {
                String avatarUrl = category.getAvatarUrl();
                if (avatarUrl != null && avatarUrl.startsWith("#")) {
                    String fileName = avatarUrl.substring(1);
                    // 先检查本地缓存
                    if (urlCache.containsKey(fileName)) {
                        category.setAvatarUrl(urlCache.get(fileName));
                    } else {
                        categoriesToProcess.put(category, fileName);
                    }
                }
                
                // // 异步增加访问量
                // incrementViewCountAsync(category.getId());
            }
            
            // 如果有需要处理的URL，使用并行处理
            if (!categoriesToProcess.isEmpty()) {
                // 使用CompletableFuture并行处理所有URL请求
                List<CompletableFuture<Void>> futures = categoriesToProcess.entrySet().stream()
                    .map(entry -> CompletableFuture.runAsync(() -> {
                        try {
                            QuestionCategory category = entry.getKey();
                            String fileName = entry.getValue();
                            String realUrl = serviceImageFeign.getPreviewUrl(fileName);
                            // 更新URL和缓存
                            category.setAvatarUrl(realUrl);
                            urlCache.put(fileName, realUrl);
                        } catch (Exception e) {
                            // 如果获取失败，保持原有URL不变
                        }
                    }))
                    .toList();
                
                // 等待所有异步任务完成
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        }

        return result;
    }
    
    /**
     * 异步增加分类的访问量
     * @param categoryId 分类ID
     */
    @Async
    public void incrementViewCountAsync(Long categoryId) {
        try {
            questionCategoryMapper.incrementViewCount(categoryId);
        } catch (Exception e) {
            // 忽略异常，不影响主流程
        }
    }
}
