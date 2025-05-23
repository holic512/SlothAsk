/**
 * File Name: GetUserQuestionServiceImpl.java
 * Description: 获取用户问题服务实现类
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.config.ViewCountConfig.ViewCountConfig;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.user.commonService.IdConversionService;
import org.example.servicequestion.user.question.dto.*;
import org.example.servicequestion.user.question.mapper.UserQuestionCategoryMapper;
import org.example.servicequestion.user.question.mapper.UserQuestionQuestionMapper;
import org.example.servicequestion.user.question.mapper.UserQuestionTagMapper;
import org.example.servicequestion.user.question.service.GetUserQuestionService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GetUserQuestionServiceImpl implements GetUserQuestionService {

    private final static String QUESTION_CACHE_KEY = RedisConfig.getKey() + "Question:Detail:";
    private final static String ANSWER_CACHE_KEY = RedisConfig.getKey() + "Question:Answer:";
    private final static String CATEGORY_CACHE_KEY = RedisConfig.getKey() + "Category:Name:";
    private final static String TAG_CACHE_KEY = RedisConfig.getKey() + "Tag:Detail:";

    private static final String SHORT_Q_CACHE_KEY = RedisConfig.getKey() + "Question:ShortDetail:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserQuestionQuestionMapper userQuestionQuestionMapper;
    private final UserQuestionCategoryMapper userQuestionCategoryMapper;
    private final UserQuestionTagMapper userQuestionTagMapper;

    private final IdConversionService idConversionService;

    @Autowired
    public GetUserQuestionServiceImpl(
            RedisTemplate<String, Object> redisTemplate,
            UserQuestionQuestionMapper userQuestionQuestionMapper,
            UserQuestionCategoryMapper userQuestionCategoryMapper,
            UserQuestionTagMapper userQuestionTagMapper,
            IdConversionService idConversionService
    ) {
        this.redisTemplate = redisTemplate;
        this.userQuestionQuestionMapper = userQuestionQuestionMapper;
        this.userQuestionCategoryMapper = userQuestionCategoryMapper;
        this.userQuestionTagMapper = userQuestionTagMapper;
        this.idConversionService = idConversionService;
    }

    // // 根据VirtualId获取原始ID
    // public Long getOriginalIdFromVirtualId(String virtualId) {
    //     String originalIdStr = (String) redisTemplate.opsForValue().get(RedisKey.QUESTION_VID_KEY + virtualId);
    //     Long originalId = null;
    //
    //     if (originalIdStr != null) {
    //         // 清理潜在的双引号（如 Redis 中存的是 "\"1\""）
    //         String cleaned = originalIdStr.replaceAll("^\"|\"$", "");
    //         if (cleaned.matches("\\d+")) {
    //             originalId = Long.parseLong(cleaned);
    //         }
    //     }
    //
    //     if (originalId == null) {
    //         originalId = IdEncryptor.decryptId(virtualId);
    //         if (originalId != null) {
    //             redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
    //             redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + originalId, virtualId, 1, TimeUnit.DAYS);
    //         }
    //     }
    //
    //     return originalId;
    // }


    @Override
    public QuestionDTO getQuestionByVirtualId(String virtualId, Long originalId) {

        // 1.  尝试从缓存获取题目信息
        String questionCacheKey = QUESTION_CACHE_KEY + originalId;
        Object cachedValue = redisTemplate.opsForValue().get(questionCacheKey);
        QuestionDTO questionDTO = null;

        // 处理缓存中的数据
        if (cachedValue != null) {
            if (cachedValue instanceof QuestionDTO) {
                questionDTO = (QuestionDTO) cachedValue;
            } else if (cachedValue instanceof Map) {
                ObjectMapper mapper = new ObjectMapper();
                questionDTO = mapper.convertValue(cachedValue, QuestionDTO.class);
            }
        }

        // 3. // 缓存未命中，从数据库查询
        if (questionDTO == null) {
            // 只查询需要的字段，排除answer字段
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("title", "content", "difficulty", "type",
                            "category_id", "tag_category_ids", "view_count",
                            "project_id", "status")
                    .eq("id", originalId);

            Question question = userQuestionQuestionMapper.selectOne(queryWrapper);
            if (question != null) {
                // 转换为DTO
                questionDTO = QuestionDTO.fromEntity(question);
                // 设置虚拟ID
                questionDTO.setVirtualId(virtualId);

                // 获取分类名称
                Long categoryId = questionDTO.getCategoryId();
                if (categoryId != null) {
                    String categoryName = getCategoryNameById(categoryId);
                    questionDTO.setCategoryName(categoryName);
                }

                // 获取标签信息
                List<Integer> tagCategoryIds = questionDTO.getTagCategoryIds();
                if (tagCategoryIds != null && !tagCategoryIds.isEmpty()) {
                    List<TagDTO> tags = getTagsByIds(tagCategoryIds);
                    questionDTO.setTags(tags);
                }

                // 进行 然后再缓存
                redisTemplate.opsForValue().set(questionCacheKey, questionDTO, 30, TimeUnit.MINUTES);
            }
        }

        return questionDTO;
    }

    @Override
    public String getQuestionAnswerByVirtualId(String virtualId) {
        // 解析虚拟ID
        long originalId = idConversionService.getOriginalIdFromVirtualId(virtualId);

        // 尝试从缓存获取答案
        String answerCacheKey = ANSWER_CACHE_KEY + originalId;
        String answer = (String) redisTemplate.opsForValue().get(answerCacheKey);

        if (answer == null) {
            // 缓存未命中，只查询answer字段
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("answer")
                    .eq("id", originalId);

            Question question = userQuestionQuestionMapper.selectOne(queryWrapper);
            if (question != null) {
                answer = question.getAnswer();
                if (answer != null) {
                    // 存入缓存，设置30分钟过期
                    redisTemplate.opsForValue().set(answerCacheKey, answer, 30, TimeUnit.MINUTES);
                }
            }
        }

        return answer;
    }

    // 根据分类ID获取分类名称，使用缓存优化性能
    private String getCategoryNameById(Long categoryId) {
        String cacheKey = CATEGORY_CACHE_KEY + categoryId;
        String categoryName = (String) redisTemplate.opsForValue().get(cacheKey);

        if (categoryName == null) {
            // 缓存未命中，从数据库查询
            categoryName = userQuestionCategoryMapper.selectNameById(categoryId);
            if (categoryName != null) {
                // 存入缓存，设置1天过期（分类信息变化不频繁）
                redisTemplate.opsForValue().set(cacheKey, categoryName, 1, TimeUnit.DAYS);
            }
        }

        return categoryName;
    }

    // 根据标签ID列表获取标签信息，使用缓存优化性能
    private List<TagDTO> getTagsByIds(List<Integer> tagCategoryIds) {
        if (tagCategoryIds == null || tagCategoryIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<TagDTO> resultTags = new ArrayList<>();
        List<Long> missingTagIds = new ArrayList<>();

        // 将Integer类型的ID转换为Long类型
        List<Long> tagIds = tagCategoryIds.stream()
                .map(Long::valueOf)
                .toList();

        // 先尝试从缓存获取每个标签
        for (Long tagId : tagIds) {
            String tagCacheKey = TAG_CACHE_KEY + tagId;
            Object cachedTag = redisTemplate.opsForValue().get(tagCacheKey);

            if (cachedTag != null) {
                if (cachedTag instanceof TagDTO) {
                    resultTags.add((TagDTO) cachedTag);
                } else if (cachedTag instanceof Map) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        TagDTO tag = mapper.convertValue(cachedTag, TagDTO.class);
                        resultTags.add(tag);
                    } catch (IllegalArgumentException e) {
                        // 转换失败，需要重新查询
                        missingTagIds.add(tagId);
                    }
                }
            } else {
                // 缓存未命中
                missingTagIds.add(tagId);
            }
        }

        // 对于缓存未命中的标签，批量查询数据库
        if (!missingTagIds.isEmpty()) {
            List<TagDTO> dbTags = userQuestionTagMapper.selectBatchByIds(missingTagIds);
            if (dbTags != null && !dbTags.isEmpty()) {
                // 将查询结果存入缓存
                for (TagDTO tag : dbTags) {
                    String tagCacheKey = TAG_CACHE_KEY + tag.getId();
                    // 标签信息变化不频繁，缓存时间可以设置长一些
                    redisTemplate.opsForValue().set(tagCacheKey, tag, 1, TimeUnit.DAYS);
                    resultTags.add(tag);
                }
            }
        }

        return resultTags;
    }

    @Override
    public QuestionListDTO getCategoryQuestionsByVirtualId(String virtualId, int page) {
        // 固定每页显示20条记录
        final int pageSize = 10;

        // 缓存键
        final String CATEGORY_QUESTIONS_CACHE_KEY = RedisConfig.getKey() + "Question:CategoryQuestions:";
        final String CATEGORY_COUNT_CACHE_KEY = RedisConfig.getKey() + "Category:Count:";
        final String QUESTION_INDEX_CACHE_KEY = RedisConfig.getKey() + "Question:CategoryIndex:";

        // 1.解析虚拟ID
        long originalId = idConversionService.getOriginalIdFromVirtualId(virtualId);

        // 2. 尝试从缓存获取完整的结果
        String resultCacheKey = CATEGORY_QUESTIONS_CACHE_KEY + originalId + ":" + page + ":" + pageSize;
        Object cachedResult = redisTemplate.opsForValue().get(resultCacheKey);

        if (cachedResult != null) {
            try {
                if (cachedResult instanceof QuestionListDTO) {
                    return (QuestionListDTO) cachedResult;
                } else if (cachedResult instanceof Map) {
                    // 处理从Redis获取的Map对象
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.convertValue(cachedResult, QuestionListDTO.class);
                }
            } catch (Exception e) {
                // 转换失败，继续执行后续逻辑
            }
        }

        // 3. 获取当前题目信息 (只查询必要的字段)
        Question currentQuestion = null;
        String questionCacheKey = SHORT_Q_CACHE_KEY + originalId;
        Object cachedQuestion = redisTemplate.opsForValue().get(questionCacheKey);

        if (cachedQuestion != null) {
            try {
                if (cachedQuestion instanceof Question) {
                    currentQuestion = (Question) cachedQuestion;
                } else if (cachedQuestion instanceof Map) {
                    ObjectMapper mapper = new ObjectMapper();
                    currentQuestion = mapper.convertValue(cachedQuestion, Question.class);
                }
            } catch (Exception e) {
            }
        }

        if (currentQuestion == null) {
            // 只查询需要的字段，减少数据传输量
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "category_id", "title", "type", "difficulty", "tag_category_ids")
                    .eq("id", originalId);

            currentQuestion = userQuestionQuestionMapper.selectOne(queryWrapper);
            if (currentQuestion == null) {
                return null;
            }

            // 缓存题目基本信息，设置较短的过期时间
            redisTemplate.opsForValue().set(questionCacheKey, currentQuestion, 30, TimeUnit.MINUTES);
        }

        // 4. 获取分类ID
        Long categoryId = currentQuestion.getCategoryId();

        // 5. 获取分类名称 (使用缓存)
        String categoryName = getCategoryNameById(categoryId);

        // 6. 获取分类下所有题目的总数 (使用缓存)
        String categoryCountKey = CATEGORY_COUNT_CACHE_KEY + categoryId;
        Integer totalCount = (Integer) redisTemplate.opsForValue().get(categoryCountKey);

        if (totalCount == null) {
            totalCount = userQuestionQuestionMapper.countByCategoryId(categoryId);
            // 缓存分类题目总数，设置较长的过期时间，因为这个数据变化不频繁
            redisTemplate.opsForValue().set(categoryCountKey, totalCount, 60, TimeUnit.MINUTES);
        }

        // 7. 获取当前题目在分类中的索引位置 (使用缓存)
        String indexKey = QUESTION_INDEX_CACHE_KEY + categoryId + ":" + originalId;
        Integer currentIndex = (Integer) redisTemplate.opsForValue().get(indexKey);

        if (currentIndex == null) {
            currentIndex = userQuestionQuestionMapper.findIndexInCategoryById(originalId, categoryId);
            // 缓存题目在分类中的索引位置，设置较长的过期时间，因为排序通常不会变
            redisTemplate.opsForValue().set(indexKey, currentIndex, 1, TimeUnit.DAYS);
        }

        // 8. 计算当前题目所在页码
        int questionPage = (currentIndex / pageSize) + 1;

        // 9. 如果请求的是0或负数页码，设为1
        if (page <= 0) {
            page = 1;
        }

        // 10. 计算总页数
        int totalPages = (totalCount + pageSize - 1) / pageSize;

        // 11. 如果请求的页码超过总页数，则设为最后一页
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        // 12. 计算当前页的起始索引
        int startIndex = (page - 1) * pageSize;

        // 13. 尝试从缓存获取当前页的题目列表
        String pageKey = CATEGORY_QUESTIONS_CACHE_KEY + categoryId + ":" + page + ":" + pageSize;
        List<Question> pageQuestions = null;
        Object cachedPageQuestions = redisTemplate.opsForValue().get(pageKey);

        if (cachedPageQuestions != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                if (cachedPageQuestions instanceof List) {
                    // 直接转换List中的每个元素
                    List<?> cachedList = (List<?>) cachedPageQuestions;
                    pageQuestions = new ArrayList<>(cachedList.size());

                    for (Object item : cachedList) {
                        if (item instanceof Question) {
                            pageQuestions.add((Question) item);
                        } else if (item instanceof Map) {
                            Question q = mapper.convertValue(item, Question.class);
                            pageQuestions.add(q);
                        }
                    }
                } else if (cachedPageQuestions instanceof Collection) {
                    // 使用Jackson转换整个集合
                    pageQuestions = mapper.convertValue(cachedPageQuestions,
                            mapper.getTypeFactory().constructCollectionType(List.class, Question.class));
                }
            } catch (Exception e) {
                pageQuestions = null; // 确保在转换失败时重新查询
            }
        }

        if (pageQuestions == null) {
            // 从数据库获取当前页的题目列表，包含标签字段
            QueryWrapper<Question> pageQueryWrapper = new QueryWrapper<>();
            pageQueryWrapper.select("id", "title", "type", "difficulty", "tag_category_ids")
                    .eq("category_id", categoryId)
                    .orderByAsc("id")
                    .last("LIMIT " + startIndex + ", " + pageSize);

            pageQuestions = userQuestionQuestionMapper.selectList(pageQueryWrapper);

            // 缓存当前页的题目列表
            redisTemplate.opsForValue().set(pageKey, pageQuestions, 30, TimeUnit.MINUTES);
        }

        // 14. 转换为DTO列表 (使用安全的方式处理可能的Map对象)
        Long finalOriginalId = originalId;
        List<QuestionBriefDTO> questionDTOs = new ArrayList<>(pageQuestions.size());

        // 记录当前页面中的序号，从1开始
        int sequentialIndex = startIndex + 1;

        for (Object questionObj : pageQuestions) {
            try {
                Question q;
                if (questionObj instanceof Question) {
                    q = (Question) questionObj;
                } else if (questionObj instanceof Map) {
                    // 处理Map对象
                    ObjectMapper mapper = new ObjectMapper();
                    q = mapper.convertValue(questionObj, Question.class);
                } else {
                    // 跳过无法处理的对象
                    continue;
                }

                QuestionBriefDTO dto = new QuestionBriefDTO();
                // 直接将递进的序号设置为id
                dto.setId((long) sequentialIndex++);

                // 获取虚拟ID (使用缓存)
                String qVirtualId = (String) redisTemplate.opsForValue().get(RedisKey.QUESTION_VID_KEY + q.getId());
                if (qVirtualId == null) {
                    qVirtualId = IdEncryptor.encryptId(q.getId());
                    // 缓存虚拟ID
                    redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + q.getId(), qVirtualId, 1, TimeUnit.DAYS);
                    redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + qVirtualId, String.valueOf(q.getId()), 1, TimeUnit.DAYS);
                }

                dto.setVirtualId(qVirtualId);
                dto.setTitle(q.getTitle());
                dto.setType(q.getType());
                dto.setDifficulty(q.getDifficulty());
                dto.setCurrent(q.getId().equals(finalOriginalId));

                questionDTOs.add(dto);
            } catch (Exception e) {
                // 处理异常
                System.out.println(e.getMessage());
            }
        }

        // 15. 构建结果
        QuestionListDTO result = new QuestionListDTO();
        result.setCurrentIndex(currentIndex + 1); // 转为从1开始计数
        result.setTotalCount(totalCount);
        result.setCurrentPage(page);
        result.setTotalPages(totalPages);
        result.setPageSize(pageSize);
        result.setCategoryId(categoryId);
        result.setCategoryName(categoryName);
        result.setQuestions(questionDTOs);

        // 添加当前题目所在页码信息，方便前端判断
        result.setQuestionPage(questionPage);

        // 16. 缓存最终结果
        redisTemplate.opsForValue().set(resultCacheKey, result, 15, TimeUnit.MINUTES);

        return result;
    }

    @Override
    @Async
    public void recordQuestionView(Long originalId, String userId) {
        try {

            // 2. 检查用户是否在防抖时间内访问过该题目
            String userViewKey = ViewCountConfig.USER_VIEW_RECORD_KEY + userId + ":" + originalId;
            Boolean hasViewedRecently = redisTemplate.hasKey(userViewKey);

            // 如果用户最近没有访问过这个题目，记录访问并设置过期时间
            if (Boolean.FALSE.equals(hasViewedRecently)) {
                // 记录用户访问，并设置过期时间
                redisTemplate.opsForValue().set(userViewKey, "1", ViewCountConfig.DEBOUNCE_INTERVAL, TimeUnit.SECONDS);

                // 增加题目访问计数
                String viewCountKey = ViewCountConfig.VIEW_COUNT_INCR_KEY + originalId;
                redisTemplate.opsForValue().increment(viewCountKey, 1L);
            }
        } catch (Exception e) {
            // 记录日志但不影响主流程
            System.err.println("记录题目访问量失败: " + e.getMessage());
        }
    }

    /**
     * 定时任务：每5分钟将Redis中的访问量数据同步到数据库
     */
    @Scheduled(fixedDelay = ViewCountConfig.SYNC_INTERVAL)
    public void syncViewCountToDatabase() {
        try {
            // 1. 获取所有需要更新的题目ID
            Set<String> keys = redisTemplate.keys(ViewCountConfig.VIEW_COUNT_INCR_KEY + "*");
            if (keys == null || keys.isEmpty()) {
                return;
            }

            // 2. 批量获取每个题目的增量访问量
            for (String key : keys) {
                Object value = redisTemplate.opsForValue().get(key);
                if (value == null) {
                    continue;
                }

                // 提取题目ID
                String idStr = key.substring(ViewCountConfig.VIEW_COUNT_INCR_KEY.length());
                Long questionId = Long.parseLong(idStr);

                // 获取增量值
                long increment = 0L;
                if (value instanceof Integer) {
                    increment = ((Integer) value).longValue();
                } else if (value instanceof Long) {
                    increment = (Long) value;
                } else if (value instanceof String) {
                    increment = Long.parseLong((String) value);
                }

                if (increment > 0) {
                    // 3. 更新数据库中的访问量
                    userQuestionQuestionMapper.incrementViewCount(questionId, increment);

                    // 4. 删除Redis中的增量记录
                    redisTemplate.delete(key);
                }
            }
        } catch (Exception e) {
            // 记录日志但不中断程序
            System.err.println("同步题目访问量到数据库失败: " + e.getMessage());
        }
    }

    @Override
    public QuestionResponseDTO getQuestionWithRealId(String virtualId) {
        // 解析虚拟ID获取原始ID
        Long originalId = idConversionService.getOriginalIdFromVirtualId(virtualId);
        if (originalId == null) {
            return null;
        }

        // 获取问题信息
        QuestionDTO questionDTO = getQuestionByVirtualId(virtualId, originalId);
        if (questionDTO == null) {
            return null;
        }

        // 封装问题信息和真实ID
        return new QuestionResponseDTO(questionDTO, originalId);
    }

    @Override
    public List<HotQuestionDTO> getHotQuestionList() {
        // 定义Redis缓存键和过期时间
        final String HOT_QUESTIONS_CACHE_KEY = RedisConfig.getKey() + "Question:HotList";
        final long CACHE_EXPIRE_TIME = 12; // 12小时
        
        // 首先尝试从缓存获取
        Object cachedValue = redisTemplate.opsForValue().get(HOT_QUESTIONS_CACHE_KEY);
        if (cachedValue != null) {
            try {
                if (cachedValue instanceof List) {
                    // 直接返回缓存的列表
                    return (List<HotQuestionDTO>) cachedValue;
                } else if (cachedValue instanceof Collection) {
                    // 尝试转换集合
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.convertValue(cachedValue,
                            mapper.getTypeFactory().constructCollectionType(List.class, HotQuestionDTO.class));
                }
            } catch (Exception e) {
                // 转换失败，将从数据库重新查询
                System.err.println("缓存转换失败: " + e.getMessage());
            }
        }
        
        // 缓存未命中，从数据库查询
        // 只查询需要的字段：id, title, view_count
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "view_count")
                .orderByDesc("view_count")
                .last("LIMIT 10"); // 只取前10条
        
        List<Question> hotQuestions = userQuestionQuestionMapper.selectList(queryWrapper);
        
        // 转换为DTO
        List<HotQuestionDTO> hotQuestionDTOs = hotQuestions.stream()
                .map(q -> {
                    // 获取虚拟ID (使用缓存)
                    String qVirtualId = (String) redisTemplate.opsForValue().get(RedisKey.QUESTION_VID_KEY + q.getId());
                    if (qVirtualId == null) {
                        qVirtualId = IdEncryptor.encryptId(q.getId());
                        // 缓存虚拟ID
                        redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + q.getId(), qVirtualId, 1, TimeUnit.DAYS);
                        redisTemplate.opsForValue().set(RedisKey.QUESTION_VID_KEY + qVirtualId, String.valueOf(q.getId()), 1, TimeUnit.DAYS);
                    }
                    
                    // 创建热门题目DTO
                    return new HotQuestionDTO(
                            qVirtualId,
                            q.getTitle(),
                            q.getViewCount()
                    );
                })
                .collect(Collectors.toList());
        
        // 缓存结果，设置较长的过期时间
        redisTemplate.opsForValue().set(HOT_QUESTIONS_CACHE_KEY, hotQuestionDTOs, CACHE_EXPIRE_TIME, TimeUnit.HOURS);
        
        return hotQuestionDTOs;
    }

}
