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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.user.category.dto.CategoryIdAndNameDto;
import org.example.servicequestion.user.category.dto.TagIdAndNameDto;
import org.example.servicequestion.user.category.mapper.QuestionCategoryMapper;
import org.example.servicequestion.user.category.mapper.UserQuestionMapper;
import org.example.servicequestion.user.category.request.GetQuestionListRequest;
import org.example.servicequestion.user.category.service.GetUserCategoryService;
import org.example.servicequestion.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GetUserCategoryServiceImpl implements GetUserCategoryService {

    private final QuestionCategoryMapper questionCategoryMapper;
    private final ServiceImageFeign serviceImageFeign;
    private final UserQuestionMapper userQuestionMapper;
    private final RedisTemplate<String, Object> redisTemplate;


    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";


    @Autowired
    GetUserCategoryServiceImpl(QuestionCategoryMapper questionCategoryMapper, ServiceImageFeign serviceImageFeign,
                               UserQuestionMapper userQuestionMapper,
                               RedisTemplate<String, Object> redisTemplate) {
        this.questionCategoryMapper = questionCategoryMapper;
        this.serviceImageFeign = serviceImageFeign;
        this.userQuestionMapper = userQuestionMapper;
        this.redisTemplate = redisTemplate;
    }

    // 定义推荐分类缓存的基础 Key（单独的键名）
    private final static String RECOMMENDED_CATEGORIES_KEY = RedisConfig.getKey() + "recommendedCategories";
    // 定义分类图片 URL 缓存的 Key 前缀
    private final static String IMG_URL_KEY = RedisConfig.getKey() + "Category:ImgURl:";


    @Override
    public List<QuestionCategory> getRecommendedCategories(Long projectId) {
        // 根据 projectId 构造完整的 Redis key
        String redisKey = projectId != null
                ? RECOMMENDED_CATEGORIES_KEY + ":" + projectId
                : RECOMMENDED_CATEGORIES_KEY + ":all";

        // 先从 Redis 获取推荐分类列表缓存
        List<QuestionCategory> result = (List<QuestionCategory>) redisTemplate.opsForValue().get(redisKey);
        if (result != null && !result.isEmpty()) {
            return result;
        }

        // 缓存未命中，根据是否传入 projectId 查询数据
        if (projectId != null) {
            result = questionCategoryMapper.findRecommendedCategoriesByProjectId(projectId);
        } else {
            result = questionCategoryMapper.findRecommendedCategories();
        }

        // 遍历结果，对每个分类的图片地址进行处理
        if (result != null && !result.isEmpty()) {
            // 收集需要处理的图片文件名映射（未缓存到 Redis 的图片）
            Map<QuestionCategory, String> categoriesToProcess = new ConcurrentHashMap<>();
            for (QuestionCategory category : result) {
                String avatarUrl = category.getAvatarUrl();
                if (avatarUrl != null && avatarUrl.startsWith("#")) {
                    // 截取文件名，假设 "#" 后面是图片文件名
                    String fileName = avatarUrl.substring(1);
                    // 构造图片缓存的完整 Key
                    String redisImgUrlKey = IMG_URL_KEY + fileName;
                    // 尝试从 Redis 中获取缓存的图片 URL
                    String cachedImgUrl = (String) redisTemplate.opsForValue().get(redisImgUrlKey);
                    if (cachedImgUrl != null) {
                        // 如果缓存存在，则直接更新分类的 avatarUrl
                        category.setAvatarUrl(cachedImgUrl);
                    } else {
                        // 否则，将该分类放入待处理集合
                        categoriesToProcess.put(category, fileName);
                    }
                }
            }

            // 对待处理的图片地址使用并行异步处理，批量调用远程服务获取真实 URL
            if (!categoriesToProcess.isEmpty()) {
                List<CompletableFuture<Void>> futures = categoriesToProcess.entrySet().stream()
                        .map(entry -> CompletableFuture.runAsync(() -> {
                            try {
                                QuestionCategory category = entry.getKey();
                                String fileName = entry.getValue();
                                // 调用远程服务获取图片的真实 URL
                                String realUrl = serviceImageFeign.getPreviewUrl(fileName);
                                // 更新分类的 avatarUrl
                                category.setAvatarUrl(realUrl);
                                // 写入 Redis 缓存，设置 10 分钟过期时间
                                redisTemplate.opsForValue().set(IMG_URL_KEY + fileName, realUrl, 10, TimeUnit.MINUTES);
                            } catch (Exception e) {
                                // 获取失败时保持原有 URL 不变，可记录日志便于调试
                            }
                        }))
                        .collect(Collectors.toList());
                // 等待所有异步任务完成
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        }

        // 将查询结果写入 Redis 缓存，设置 10 分钟过期时间
        redisTemplate.opsForValue().set(redisKey, result, 10, TimeUnit.MINUTES);

        return result;
    }


    @Override
    public List<CategoryIdAndNameDto> getCategoryIdAndName(Long projectId) {
        List<CategoryIdAndNameDto> result;
        // 当存在项目ID时，限定条件为project_id,不存在项目ID时，从项目id为1分类中获取
        if (projectId != null) {
            result = questionCategoryMapper.getCategoryIdAndNameByProjectId(projectId);
        } else {
            result = questionCategoryMapper.getCategoryIdAndNameByProjectId(1L);
        }
        return result;
    }

    @Override
    public List<TagIdAndNameDto> getTagIdAndName(Long projectId) {
        List<TagIdAndNameDto> result;
        // 当存在项目ID时，限定条件为project_id,不存在项目ID时，从项目id为1分类中获取
        if (projectId != null) {
            result = questionCategoryMapper.getTagIdAndName(projectId);
        } else {
            result = questionCategoryMapper.getTagIdAndName(1L);
        }
        return result;
    }

    @Override
    public Page<Question> getQuestionList(Long projectId, GetQuestionListRequest request) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();

        // 获取 Question 表的所有字段
        TableInfo tableInfo = TableInfoHelper.getTableInfo(Question.class);
        if (tableInfo != null) {
            List<String> allFields = tableInfo.getFieldList().stream()
                    .map(TableFieldInfo::getColumn)  // 获取数据库字段名
                    .collect(Collectors.toList());
            // 添加主键字段
            allFields.add(tableInfo.getKeyColumn());

            // 排除无用字段
            allFields.remove("content");
            allFields.remove("answer");
            allFields.remove("project_id");
            allFields.remove("status");
            allFields.remove("createTime");
            allFields.remove("updateTime");

            // 设置查询字段
            queryWrapper.select(allFields.toArray(new String[0]));
        }

        // 如果关键字不为空，则在标题和内容中模糊搜索
        if (StringUtils.hasText(request.getSearchText())) {
            queryWrapper.and(wrapper ->
                    wrapper.like("title", request.getSearchText())
                            .or().like("content", request.getSearchText())
            );
        }

        if (request.getFilterCategory() != null) {
            queryWrapper.eq("category_id", request.getFilterCategory());
        }
        if (request.getFilterDifficulty() != null) {
            queryWrapper.eq("difficulty", request.getFilterDifficulty());
        }
        if (request.getFilterType() != null) {
            queryWrapper.eq("type", request.getFilterType());
        }

        // 处理 tag_id 的 JSON 数组查询
        if (request.getFilterTags() != null && !request.getFilterTags().isEmpty()) {
            for (Integer tag : request.getFilterTags()) {
                queryWrapper.apply("JSON_CONTAINS(tag_category_id, '" + tag + "')");
            }
        }

        // 确保状态为 启用
        queryWrapper.eq("status", 1);

        Page<Question> Ppage = new Page<>(request.getPageNum(), 20);
        Page<Question> page = userQuestionMapper.selectPage(Ppage, queryWrapper);
        List<Question> questionList = page.getRecords();

        // 批量处理虚拟ID生成和Redis存储
        Map<String, String> virtualIdMap = new HashMap<>();

        questionList.parallelStream().forEach(question -> {
            String originalId = question.getId().toString();
            String redisKeyOriginal = VidKey + originalId;

            // 从 Redis 中获取虚拟 id
            String virtualId = (String) redisTemplate.opsForValue().get(redisKeyOriginal);

            if (virtualId == null) {
                // 如果没有映射，则生成新的虚拟 id
                virtualId = UuidUtil.generate8DigitNumericId();
                // 建立双向映射：原始 id -> 虚拟 id 和 虚拟 id -> 原始 id
                virtualIdMap.put(redisKeyOriginal, virtualId);
                virtualIdMap.put(VidKey + virtualId, originalId);
            }

            // 替换 Question 对象中的 id 为虚拟 id
            question.setId(Long.valueOf(virtualId));
        });

        // 批量存储虚拟ID到Redis
        if (!virtualIdMap.isEmpty()) {
            redisTemplate.opsForValue().multiSet(virtualIdMap);
        }

        return page;
    }


    /**
     * 异步增加分类的访问量
     *
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
