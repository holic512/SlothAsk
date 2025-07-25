/**
 * File Name: GetUserCategoryServiceImpl.java
 * Description: 获取用户题库分类服务实现类
 * Author: holic512
 * Created Date: 2025-03-19
 * Version: 1.0
 * Usage:
 * 提供用户题库分类相关的查询服务实现
 */
package org.example.servicequestion.user.study.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.servicecommon.entity.UserDailySubmitCount;
import org.example.servicecommon.redisKey.AnswerAiKey;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.user.study.dto.CategoryIdAndNameDto;
import org.example.servicequestion.user.study.dto.HotQuestionDto;
import org.example.servicequestion.user.study.dto.TagIdAndNameDto;
import org.example.servicequestion.user.study.dto.UserSubmitCountDto;
import org.example.servicequestion.user.study.mapper.QuestionCategoryMapper;
import org.example.servicequestion.user.study.mapper.UserDailySubmitCountMapper;
import org.example.servicequestion.user.study.mapper.UserQuestionMapper;
import org.example.servicequestion.user.study.request.GetQuestionListRequest;
import org.example.servicequestion.user.study.service.GetUserStudyService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class GetUserStudyServiceImpl implements GetUserStudyService {

    private final QuestionCategoryMapper questionCategoryMapper;
    private final ServiceImageFeign serviceImageFeign;
    private final UserQuestionMapper userQuestionMapper;
    private final UserDailySubmitCountMapper userDailySubmitCountMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 问题的虚拟IdKey
    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";
    // 添加问题列表缓存key前缀
    private final static String QUESTION_LIST_CACHE_KEY = RedisConfig.getKey() + "Question:List:";
    // 设置问题列表缓存过期时间（分钟）
    private final static int QUESTION_LIST_CACHE_EXPIRY = 5;
    // 下一题ID缓存键前缀
    private final static String NEXT_QUESTION_CACHE_KEY = RedisConfig.getKey() + "Question:Next:";
    // 下一题缓存过期时间（分钟）
    private final static int NEXT_QUESTION_CACHE_EXPIRY = 30;

    // 添加分类ID缓存键前缀
    private final static String QUESTION_CATEGORY_CACHE_KEY = RedisConfig.getKey() + "Question:Category:";
    // 分类缓存过期时间（小时）
    private final static int QUESTION_CATEGORY_CACHE_EXPIRY = 24;

    @Autowired
    GetUserStudyServiceImpl(QuestionCategoryMapper questionCategoryMapper, ServiceImageFeign serviceImageFeign,
                            UserQuestionMapper userQuestionMapper,
                            UserDailySubmitCountMapper userDailySubmitCountMapper,
                            RedisTemplate<String, Object> redisTemplate,
                            ObjectMapper objectMapper) {
        this.questionCategoryMapper = questionCategoryMapper;
        this.serviceImageFeign = serviceImageFeign;
        this.userQuestionMapper = userQuestionMapper;
        this.userDailySubmitCountMapper = userDailySubmitCountMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
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
                        .toList();
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
        // 构建缓存key，包含所有查询参数
        StringBuilder cacheKeyBuilder = new StringBuilder(QUESTION_LIST_CACHE_KEY);
        cacheKeyBuilder.append(projectId != null ? projectId : "null")
                .append(":")
                .append(request.getPageNum())
                .append(":")
                .append("match:").append(request.getMatchAllConditions()).append(":");
        
        if (StringUtils.hasText(request.getSearchText())) {
            cacheKeyBuilder.append("st:").append(request.getSearchText()).append(":");
        }
        if (request.getFilterCategoryEquals() != null) {
            cacheKeyBuilder.append("fce:").append(request.getFilterCategoryEquals()).append(":");
        }
        if (request.getFilterCategoryNotEquals() != null) {
            cacheKeyBuilder.append("fcne:").append(request.getFilterCategoryNotEquals()).append(":");
        }
        if (request.getFilterDifficultyEquals() != null) {
            cacheKeyBuilder.append("fde:").append(request.getFilterDifficultyEquals()).append(":");
        }
        if (request.getFilterDifficultyNotEquals() != null) {
            cacheKeyBuilder.append("fdne:").append(request.getFilterDifficultyNotEquals()).append(":");
        }
        if (request.getFilterTypeEquals() != null) {
            cacheKeyBuilder.append("fte:").append(request.getFilterTypeEquals()).append(":");
        }
        if (request.getFilterTypeNotEquals() != null) {
            cacheKeyBuilder.append("ftne:").append(request.getFilterTypeNotEquals()).append(":");
        }
        if (request.getFilterTags() != null && !request.getFilterTags().isEmpty()) {
            cacheKeyBuilder.append("tags:");
            for (Integer tag : request.getFilterTags()) {
                cacheKeyBuilder.append(tag).append(",");
            }
        }
        
        String cacheKey = cacheKeyBuilder.toString();
        
        // 先尝试从缓存获取结果
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            try {
                // 将缓存数据转换为JSON字符串，然后反序列化为Page<Question>对象
                String jsonString = objectMapper.writeValueAsString(cachedData);
                return objectMapper.readValue(jsonString, new TypeReference<Page<Question>>() {});
            } catch (Exception e) {
                // 转换失败，继续执行数据库查询
                // 可以记录日志，但不影响主流程
            }
        }
        
        // 缓存未命中或转换失败，执行数据库查询
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
            allFields.remove("virtual_id");

            // 设置查询字段
            queryWrapper.select(allFields.toArray(new String[0]));
        }

        // 构建筛选条件列表
        List<Consumer<QueryWrapper<Question>>> conditions = new ArrayList<>();
        
        // 如果关键字不为空，则在标题和内容中模糊搜索
        if (StringUtils.hasText(request.getSearchText())) {
            conditions.add(wrapper -> wrapper.and(w ->
                    w.like("title", request.getSearchText())
                            .or().like("content", request.getSearchText())
            ));
        }

        // 分类过滤条件
        if (request.getFilterCategoryEquals() != null) {
            conditions.add(wrapper -> wrapper.eq("category_id", request.getFilterCategoryEquals()));
        }
        if (request.getFilterCategoryNotEquals() != null) {
            conditions.add(wrapper -> wrapper.ne("category_id", request.getFilterCategoryNotEquals()));
        }
        
        // 难度过滤条件
        if (request.getFilterDifficultyEquals() != null) {
            conditions.add(wrapper -> wrapper.eq("difficulty", request.getFilterDifficultyEquals()));
        }
        if (request.getFilterDifficultyNotEquals() != null) {
            conditions.add(wrapper -> wrapper.ne("difficulty", request.getFilterDifficultyNotEquals()));
        }
        
        // 类型过滤条件
        if (request.getFilterTypeEquals() != null) {
            conditions.add(wrapper -> wrapper.eq("type", request.getFilterTypeEquals()));
        }
        if (request.getFilterTypeNotEquals() != null) {
            conditions.add(wrapper -> wrapper.ne("type", request.getFilterTypeNotEquals()));
        }

        // 处理 tag_id 的 JSON 数组查询
        if (request.getFilterTags() != null && !request.getFilterTags().isEmpty()) {
            conditions.add(wrapper -> {
                for (Integer tag : request.getFilterTags()) {
                    wrapper.apply("JSON_CONTAINS(tag_category_ids, '" + tag + "')");
                }
            });
        }
        
        // 根据匹配模式应用条件
        if (!conditions.isEmpty()) {
            if (request.getMatchAllConditions()) {
                // 全部满足模式 (AND)
                for (Consumer<QueryWrapper<Question>> condition : conditions) {
                    condition.accept(queryWrapper);
                }
            } else {
                // 任一满足模式 (OR)
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < conditions.size(); i++) {
                        if (i > 0) {
                            wrapper.or();
                        }
                        wrapper.nested(conditions.get(i));
                    }
                });
            }
        }

        // 确保状态为 启用
        queryWrapper.eq("status", 1);

        // 分页查询
        Page<Question> page = new Page<>(request.getPageNum(), 20);
        page = userQuestionMapper.selectPage(page, queryWrapper);
        List<Question> questionList = page.getRecords();

        // 预先构造需要批量获取的 Redis key 集合
        List<String> keysToFetch = questionList.stream()
                .map(question -> VidKey + question.getId())
                .collect(Collectors.toList());

        // 批量读取 Redis 数据，避免每个请求单独调用阻塞 API
        List<Object> redisResults = redisTemplate.opsForValue().multiGet(keysToFetch);

        // 预估虚拟ID映射表大小，减少扩容开销（采用线程安全的Map）
        Map<String, String> virtualIdMap = new ConcurrentHashMap<>(questionList.size() * 2);

        final int batchSize = 20; // 批量大小常量化
        final int baseStartIndex = (request.getPageNum() - 1) * batchSize + 1;

        // 同步遍历列表，设置虚拟ID与序号
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String redisKey = keysToFetch.get(i);
            // 从批量获取的结果中获取虚拟ID
            String virtualId = redisResults != null ? (String) redisResults.get(i) : null;
            if (virtualId == null) {
                // 如果缓存不存在，则生成新的虚拟ID
                virtualId = IdEncryptor.encryptId(question.getId());
                // 建立双向映射
                virtualIdMap.put(redisKey, virtualId);
                virtualIdMap.put(VidKey + virtualId, String.valueOf(question.getId()));
            }
            question.setVirtualId(virtualId);
            question.setId((long) (baseStartIndex + i));
        }

        // 异步方式批量存储生成的虚拟ID映射关系到 Redis，以避免同步阻塞
        CompletableFuture.runAsync(() -> {
            if (!virtualIdMap.isEmpty()) {
                redisTemplate.opsForValue().multiSet(virtualIdMap);
            }
        });
        
        // 将查询结果存入缓存
        try {
            redisTemplate.opsForValue().set(cacheKey, page, QUESTION_LIST_CACHE_EXPIRY, TimeUnit.MINUTES);
        } catch (Exception e) {
            // 缓存存储失败不应影响主流程
            // 可以记录日志，但继续返回结果
        }

        return page;
    }

    @Override
    public String getNextQuestionVid(String currentVid) {
        if (currentVid == null || currentVid.isEmpty()) {
            return null;
        }
        
        // 构建缓存键
        String cacheKey = NEXT_QUESTION_CACHE_KEY + currentVid;
        
        // 尝试从缓存获取
        String nextVid = (String) redisTemplate.opsForValue().get(cacheKey);
        if (nextVid != null) {
            return nextVid;
        }
        
        try {
            // 1. 从虚拟ID获取真实ID
            String realIdKey = VidKey + currentVid;
            String realIdStr = (String) redisTemplate.opsForValue().get(realIdKey);
            
            // 如果缓存中没有映射关系，尝试解密虚拟ID
            Long realId;
            if (realIdStr == null) {
                try {
                    realId = IdEncryptor.decryptId(currentVid);
                } catch (Exception e) {
                    // 无效的虚拟ID
                    return null;
                }
            } else {
                realId = Long.valueOf(realIdStr);
            }
            
            // 2. 查询当前题目分类ID（先尝试从缓存获取）
            Long queryCategoryId = null;
            String categoryIdKey = QUESTION_CATEGORY_CACHE_KEY + realId;
            String cachedCategoryId = (String) redisTemplate.opsForValue().get(categoryIdKey);
            
            if (cachedCategoryId != null) {
                queryCategoryId = Long.valueOf(cachedCategoryId);
            } else {
                // 缓存未命中，查询数据库
                Question currentQuestion = userQuestionMapper.selectById(realId);
                if (currentQuestion == null) {
                    return null; // 当前题目不存在
                }
                
                queryCategoryId = currentQuestion.getCategoryId();
                
                // 异步缓存分类ID
                final Long categoryId = queryCategoryId;
                CompletableFuture.runAsync(() -> {
                    redisTemplate.opsForValue().set(categoryIdKey, String.valueOf(categoryId), 
                        QUESTION_CATEGORY_CACHE_EXPIRY, TimeUnit.HOURS);
                });
            }
            
            // 3. 查询下一题
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", queryCategoryId);
            queryWrapper.eq("status", 1); // 确保状态为启用
            queryWrapper.gt("id", realId); // ID大于当前题目ID
            queryWrapper.orderByAsc("id"); // 按ID升序
            queryWrapper.last("LIMIT 1"); // 只获取一条记录
            
            Question nextQuestion = userQuestionMapper.selectOne(queryWrapper);
            
            // 如果没有找到下一题，可能是当前题目是该分类中的最后一题
            // 尝试查找该分类的第一题（循环回到开头）
            if (nextQuestion == null) {
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("category_id", queryCategoryId);
                queryWrapper.eq("status", 1);
                queryWrapper.orderByAsc("id");
                queryWrapper.last("LIMIT 1");
                
                nextQuestion = userQuestionMapper.selectOne(queryWrapper);
                
                // 如果仍然没有找到，说明该分类下没有题目
                if (nextQuestion == null) {
                    return null;
                }
            }
            
            // 4. 获取下一题的虚拟ID
            String nextVirtualId;
            String nextVirtualIdKey = VidKey + nextQuestion.getId();
            nextVirtualId = (String) redisTemplate.opsForValue().get(nextVirtualIdKey);
            
            // 如果缓存中没有，生成新的虚拟ID
            if (nextVirtualId == null) {
                nextVirtualId = IdEncryptor.encryptId(nextQuestion.getId());
                // 建立双向映射并异步存储到Redis
                final String finalNextVirtualId = nextVirtualId;
                Question finalNextQuestion = nextQuestion;
                CompletableFuture.runAsync(() -> {
                    redisTemplate.opsForValue().set(nextVirtualIdKey, finalNextVirtualId);
                    redisTemplate.opsForValue().set(VidKey + finalNextVirtualId, String.valueOf(finalNextQuestion.getId()));
                });
            }
            
            // 5. 缓存结果
            redisTemplate.opsForValue().set(cacheKey, nextVirtualId, NEXT_QUESTION_CACHE_EXPIRY, TimeUnit.MINUTES);
            
            return nextVirtualId;
            
        } catch (Exception e) {
            // 发生异常，记录日志但不影响主流程
            return null;
        }
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

    @Override
    public List<UserSubmitCountDto> getUserSubmitCountStats(Long userId) {
        try {
            // 1. 获取当前日期和119天前的日期
            LocalDate today = LocalDate.now();
            LocalDate startDate = today.minusDays(119);
            LocalDate endDate = today.minusDays(1); // 数据库查询到昨天为止，不包含今天
            
            // 2. 从数据库查询过去119天的提交记录（不包含今天）
            List<UserDailySubmitCount> dbRecords = userDailySubmitCountMapper
                    .selectUserSubmitCountByDateRange(userId, startDate, endDate);
            
            // 3. 将数据库记录转换为Map，便于查找
            Map<String, Integer> dbDataMap = new HashMap<>();
            for (UserDailySubmitCount record : dbRecords) {
                String dateStr = record.getSubmitDate().toString();
                dbDataMap.put(dateStr, record.getSubmitCount());
            }
            
            // 4. 查询Redis中当天的提交次数
            String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String redisKey = AnswerAiKey.DAILY_SUBMIT_COUNT_KEY + userId + ":" + todayStr;
            Integer todayCount = 0;
            try {
                Object redisValue = redisTemplate.opsForValue().get(redisKey);
                if (redisValue != null) {
                    todayCount = Integer.valueOf(redisValue.toString());
                }
            } catch (Exception e) {
                // Redis查询失败，使用默认值0
            }
            
            // 5. 构建结果列表，包含最近120天的数据
            List<UserSubmitCountDto> result = new ArrayList<>();
            
            // 从今天开始，往前推120天
            for (int i = 0; i < 120; i++) {
                LocalDate currentDate = today.minusDays(i);
                String dateStr = currentDate.toString();
                
                Integer count;
                if (i == 0) {
                    // 今天的数据只从Redis中获取
                    count = todayCount;
                } else {
                    // 其他日期使用数据库中的值
                    count = dbDataMap.getOrDefault(dateStr, 0);
                }
                
                result.add(new UserSubmitCountDto(dateStr, count));
            }
            
            return result;
            
        } catch (Exception e) {
            // 发生异常时返回空列表
            return new ArrayList<>();
        }
    }

    @Override
    public List<HotQuestionDto> getHotQuestions(Long projectId) {
        // 热门题目缓存键
        String cacheKey = RedisConfig.getKey() + "HotQuestions:" + (projectId != null ? projectId : "all");
        
        // 先尝试从缓存获取
        List<HotQuestionDto> cachedResult = (List<HotQuestionDto>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null && !cachedResult.isEmpty()) {
            return cachedResult;
        }
        
        // 构建查询条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "view_count")
                .eq("status", 1) // 只查询启用状态的题目
                .orderByDesc("view_count") // 按浏览量降序排列
                .last("LIMIT 10"); // 限制返回10条记录
        
        // 如果指定了项目ID，添加项目过滤条件
        if (projectId != null) {
            queryWrapper.eq("project_id", projectId);
        }
        
        // 查询数据库
        List<Question> questions = userQuestionMapper.selectList(queryWrapper);
        
        // 转换为DTO并生成虚拟ID
        List<HotQuestionDto> result = new ArrayList<>();
        Map<String, String> virtualIdMap = new ConcurrentHashMap<>();
        
        for (Question question : questions) {
            // 先尝试从Redis获取虚拟ID
            String redisKey = VidKey + question.getId();
            String virtualId = (String) redisTemplate.opsForValue().get(redisKey);
            
            if (virtualId == null) {
                // 如果缓存不存在，生成新的虚拟ID
                virtualId = IdEncryptor.encryptId(question.getId());
                // 建立双向映射
                virtualIdMap.put(redisKey, virtualId);
                virtualIdMap.put(VidKey + virtualId, String.valueOf(question.getId()));
            }
            
            // 创建DTO对象
            HotQuestionDto dto = new HotQuestionDto(virtualId, question.getTitle(), question.getViewCount());
            result.add(dto);
        }
        
        // 异步存储虚拟ID映射关系
        if (!virtualIdMap.isEmpty()) {
            CompletableFuture.runAsync(() -> {
                redisTemplate.opsForValue().multiSet(virtualIdMap);
            });
        }
        
        // 将结果缓存10分钟
        redisTemplate.opsForValue().set(cacheKey, result, 10, TimeUnit.MINUTES);
        
        return result;
    }

}
