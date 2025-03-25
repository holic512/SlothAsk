/**
 * File Name: GetUserQuestionServiceImpl.java
 * Description: 获取用户问题服务实现类
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.user.question.dto.QuestionBriefDTO;
import org.example.servicequestion.user.question.dto.QuestionDTO;
import org.example.servicequestion.user.question.dto.QuestionListDTO;
import org.example.servicequestion.user.question.dto.TagDTO;
import org.example.servicequestion.user.question.mapper.UserQuestionCategoryMapper;
import org.example.servicequestion.user.question.mapper.UserQuestionQuestionMapper;
import org.example.servicequestion.user.question.mapper.UserQuestionTagMapper;
import org.example.servicequestion.user.question.service.GetUserQuestionService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GetUserQuestionServiceImpl implements GetUserQuestionService {

    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";
    private final static String QUESTION_CACHE_KEY = RedisConfig.getKey() + "Question:Detail:";
    private final static String ANSWER_CACHE_KEY = RedisConfig.getKey() + "Question:Answer:";
    private final static String CATEGORY_CACHE_KEY = RedisConfig.getKey() + "Category:Name:";
    private final static String TAG_CACHE_KEY = RedisConfig.getKey() + "Tag:Detail:";

    private static final String SHORT_Q_CACHE_KEY = RedisConfig.getKey() + "Question:ShortDetail:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserQuestionQuestionMapper userQuestionQuestionMapper;
    private final UserQuestionCategoryMapper userQuestionCategoryMapper;
    private final UserQuestionTagMapper userQuestionTagMapper;

    @Autowired
    public GetUserQuestionServiceImpl(
            RedisTemplate<String, Object> redisTemplate,
            UserQuestionQuestionMapper userQuestionQuestionMapper,
            UserQuestionCategoryMapper userQuestionCategoryMapper,
            UserQuestionTagMapper userQuestionTagMapper) {
        this.redisTemplate = redisTemplate;
        this.userQuestionQuestionMapper = userQuestionQuestionMapper;
        this.userQuestionCategoryMapper = userQuestionCategoryMapper;
        this.userQuestionTagMapper = userQuestionTagMapper;
    }

    @Override
    public QuestionDTO getQuestionByVirtualId(String virtualId) {
        // 1.  先从 Redis 获取原始 ID，如果 Redis 里有，尝试解析
        String originalIdStr = (String) redisTemplate.opsForValue().get(VidKey + virtualId);
        Long originalId = null;

        if (originalIdStr != null && originalIdStr.matches("\\d+")) { // 确保是数字格式
            originalId = Long.parseLong(originalIdStr);
        }

        // 如果 Redis 没有，则解析 virtualId
        if (originalId == null) {
            originalId = IdEncryptor.decryptId(virtualId);

            // 解析成功才存入 Redis
            redisTemplate.opsForValue().set(VidKey + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(VidKey + originalId, virtualId, 1, TimeUnit.DAYS);
        }

        // 2.  尝试从缓存获取题目信息
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
        // 先从 Redis 获取原始 ID，如果 Redis 里有，尝试解析
        String originalIdStr = (String) redisTemplate.opsForValue().get(VidKey + virtualId);
        Long originalId = null;

        if (originalIdStr != null && originalIdStr.matches("\\d+")) { // 确保是数字格式
            originalId = Long.parseLong(originalIdStr);
        }

        // 如果 Redis 没有，则解析 virtualId
        if (originalId == null) {
            originalId = IdEncryptor.decryptId(virtualId);

            // 解析成功才存入 Redis
            redisTemplate.opsForValue().set(VidKey + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(VidKey + originalId, virtualId, 1, TimeUnit.DAYS);
        }

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
    public QuestionListDTO getCategoryQuestionsByVirtualId(String virtualId, int pageSize) {
        // 缓存键
        final String CATEGORY_QUESTIONS_CACHE_KEY = RedisConfig.getKey() + "Question:CategoryQuestions:";
        final String CATEGORY_COUNT_CACHE_KEY = RedisConfig.getKey() + "Category:Count:";
        final String QUESTION_INDEX_CACHE_KEY = RedisConfig.getKey() + "Question:CategoryIndex:";

        // 1. 根据虚拟ID获取真实ID (使用已有的缓存逻辑)
        String originalIdStr = (String) redisTemplate.opsForValue().get(VidKey + virtualId);
        Long originalId;

        if (originalIdStr != null && originalIdStr.matches("\\d+")) {
            originalId = Long.parseLong(originalIdStr);
        } else {
            originalId = IdEncryptor.decryptId(virtualId);

            if (originalId != null) {
                // 缓存虚拟ID和真实ID的映射关系
                redisTemplate.opsForValue().set(VidKey + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(VidKey + originalId, virtualId, 1, TimeUnit.DAYS);
            } else {
                return null; // 无效的虚拟ID
            }
        }

        // 2. 尝试从缓存获取完整的结果
        String resultCacheKey = CATEGORY_QUESTIONS_CACHE_KEY + originalId + ":" + pageSize;
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

        // 8. 计算当前页码（确保当前题目在当前页中）
        int currentPage = (currentIndex / pageSize) + 1;

        // 9. 计算总页数
        int totalPages = (totalCount + pageSize - 1) / pageSize;

        // 10. 计算当前页的起始索引
        int startIndex = (currentPage - 1) * pageSize;

        // 11. 尝试从缓存获取当前页的题目列表
        String pageKey = CATEGORY_QUESTIONS_CACHE_KEY + categoryId + ":" + currentPage + ":" + pageSize;
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
            pageQueryWrapper.select( "id","title", "type", "difficulty", "tag_category_ids")
                    .eq("category_id", categoryId)
                    .orderByAsc("id")
                    .last("LIMIT " + startIndex + ", " + pageSize);

            pageQuestions = userQuestionQuestionMapper.selectList(pageQueryWrapper);

            // 缓存当前页的题目列表
            redisTemplate.opsForValue().set(pageKey, pageQuestions, 30, TimeUnit.MINUTES);
        }


        // 12. 转换为DTO列表 (使用安全的方式处理可能的Map对象)
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
                dto.setId((long)sequentialIndex++);

                // 获取虚拟ID (使用缓存)
                String qVirtualId = (String) redisTemplate.opsForValue().get(VidKey + q.getId());
                if (qVirtualId == null) {
                    qVirtualId = IdEncryptor.encryptId(q.getId());
                    // 缓存虚拟ID
                    redisTemplate.opsForValue().set(VidKey + q.getId(), qVirtualId, 1, TimeUnit.DAYS);
                    redisTemplate.opsForValue().set(VidKey + qVirtualId, String.valueOf(q.getId()), 1, TimeUnit.DAYS);
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

        // 13. 构建结果
        QuestionListDTO result = new QuestionListDTO();
        result.setCurrentIndex(currentIndex + 1); // 转为从1开始计数
        result.setTotalCount(totalCount);
        result.setCurrentPage(currentPage);
        result.setTotalPages(totalPages);
        result.setPageSize(pageSize);
        result.setCategoryId(categoryId);
        result.setCategoryName(categoryName);
        result.setQuestions(questionDTOs);

        // 14. 缓存最终结果
        redisTemplate.opsForValue().set(resultCacheKey, result, 15, TimeUnit.MINUTES);

        return result;
    }
}
