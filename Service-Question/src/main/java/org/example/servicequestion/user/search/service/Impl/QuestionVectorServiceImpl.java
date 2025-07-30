/**
 * File Name: QuestionVectorServiceImpl.java
 * Description: 题目向量化服务实现类
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * 实现题目向量化的核心业务逻辑
 */
package org.example.servicequestion.user.search.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.servicecommon.redisKey.VectorKey;
import org.example.servicequestion.config.Qdrant.QdrantVectorService;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.feign.ServiceAiFeign;
import org.example.servicequestion.user.search.mapper.UserQuestionSearchQuestionMapper;
import org.example.servicequestion.user.search.service.QuestionVectorService;
import org.example.servicequestion.util.HtmlTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionVectorServiceImpl implements QuestionVectorService {

    // 批量处理大小
    private static final int BATCH_SIZE = 40;
    // 锁的过期时间（小时）
    private static final int LOCK_EXPIRE_HOURS = 2;
    // 当前使用的向量化策略
    private static final VectorizeStrategy CURRENT_STRATEGY = VectorizeStrategy.TITLE_CONTENT_WEIGHTED;
    private final UserQuestionSearchQuestionMapper questionMapper;
    private final ServiceAiFeign serviceAiFeign;
    private final QdrantVectorService qdrantVectorService;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    public QuestionVectorServiceImpl(UserQuestionSearchQuestionMapper questionMapper,
                                     ServiceAiFeign serviceAiFeign,
                                     QdrantVectorService qdrantVectorService,
                                     RedisTemplate<String, Object> redisTemplate) {
        this.questionMapper = questionMapper;
        this.serviceAiFeign = serviceAiFeign;
        this.qdrantVectorService = qdrantVectorService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void scheduleVectorizeUpdatedQuestions() {
        try {
            log.info("开始执行定时向量化任务");

            // 获取上次更新时间
            String lastUpdateTimeStr = (String) redisTemplate.opsForValue().get(VectorKey.LAST_UPDATE_TIME);
            LocalDateTime lastUpdateTime;

            if (lastUpdateTimeStr != null) {
                lastUpdateTime = LocalDateTime.parse(lastUpdateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else {
                // 如果没有记录，则从一天前开始
                lastUpdateTime = LocalDateTime.now().minusDays(1);
            }

            int processedCount = vectorizeQuestionsAfter(lastUpdateTime);

            // 更新最后处理时间
            redisTemplate.opsForValue().set(VectorKey.LAST_UPDATE_TIME,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            log.info("定时向量化任务完成，处理了 {} 个题目", processedCount);

        } catch (Exception e) {
            log.error("定时向量化任务执行失败", e);
        }
    }

    @Override
    public boolean manualBatchVectorizeAllQuestions() {
        // 尝试获取锁
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
                VectorKey.VECTORIZATION_LOCK,
                "processing",
                LOCK_EXPIRE_HOURS,
                TimeUnit.HOURS
        );

        if (!lockAcquired) {
            log.warn("向量化任务正在进行中，无法重复执行");
            return false;
        }

        try {
            // 异步执行向量化任务
            CompletableFuture.runAsync(() -> {
                try {
                    log.info("开始手动批量向量化所有题目");
                    int totalProcessed = vectorizeAllQuestions();
                    log.info("手动批量向量化完成，总共处理了 {} 个题目", totalProcessed);
                } catch (Exception e) {
                    log.error("手动批量向量化失败", e);
                } finally {
                    // 清除锁和进度
                    redisTemplate.delete(VectorKey.VECTORIZATION_LOCK);
                    redisTemplate.delete(VectorKey.VECTORIZATION_PROGRESS);
                }
            });

            return true;
        } catch (Exception e) {
            // 如果启动失败，释放锁
            redisTemplate.delete(VectorKey.VECTORIZATION_LOCK);
            log.error("启动手动批量向量化任务失败", e);
            return false;
        }
    }

    @Override
    public String getVectorizationProgress() {
        String progress = (String) redisTemplate.opsForValue().get(VectorKey.VECTORIZATION_PROGRESS);
        return progress != null ? progress : "0%";
    }

    @Override
    public int vectorizeQuestionsAfter(LocalDateTime lastUpdateTime) {
        int totalProcessed = 0;
        int pageNum = 1;

        while (true) {
            // 分页查询更新的题目
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "title", "content", "update_time")
                    .eq("status", 1)
                    .gt("update_time", lastUpdateTime)
                    .orderByAsc("id");

            Page<Question> page = new Page<>(pageNum, BATCH_SIZE);
            page = questionMapper.selectPage(page, queryWrapper);

            List<Question> questions = page.getRecords();
            if (questions.isEmpty()) {
                break;
            }

            // 处理当前批次
            int batchProcessed = processBatchQuestions(questions);
            totalProcessed += batchProcessed;

            log.info("处理第 {} 页，本批次处理 {} 个题目", pageNum, batchProcessed);

            // 如果是最后一页，退出循环
            if (pageNum >= page.getPages()) {
                break;
            }

            pageNum++;
        }

        return totalProcessed;
    }

    @Override
    public int vectorizeAllQuestions() {
        int totalProcessed = 0;
        int pageNum = 1;

        // 先获取总数用于计算进度
        QueryWrapper<Question> countWrapper = new QueryWrapper<>();
        countWrapper.eq("status", 1);
        Long totalCount = questionMapper.selectCount(countWrapper);

        while (true) {
            // 分页查询所有题目
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "title", "content")
                    .eq("status", 1)
                    .orderByAsc("id");

            Page<Question> page = new Page<>(pageNum, BATCH_SIZE);
            page = questionMapper.selectPage(page, queryWrapper);

            List<Question> questions = page.getRecords();
            if (questions.isEmpty()) {
                break;
            }

            // 处理当前批次
            int batchProcessed = processBatchQuestions(questions);
            totalProcessed += batchProcessed;

            // 更新进度
            double progress = (double) totalProcessed / totalCount * 100;
            redisTemplate.opsForValue().set(VectorKey.VECTORIZATION_PROGRESS,
                    String.format("%.1f%%", progress), 1, TimeUnit.HOURS);

            log.info("处理第 {} 页，本批次处理 {} 个题目，总进度: {:.1f}%",
                    pageNum, batchProcessed, progress);

            // 如果是最后一页，退出循环
            if (pageNum >= page.getPages()) {
                break;
            }

            pageNum++;
        }

        return totalProcessed;
    }

    /**
     * 组合题目的标题和内容为向量化文本
     * 支持多种向量化策略，可根据实际效果调整
     *
     * @param question 题目对象
     * @return 组合后的文本
     */
    private String combineQuestionText(Question question) {
        return combineQuestionText(question, CURRENT_STRATEGY);
    }

    /**
     * 根据指定策略组合题目文本
     *
     * @param question 题目对象
     * @param strategy 向量化策略
     * @return 组合后的文本
     */
    private String combineQuestionText(Question question, VectorizeStrategy strategy) {
        switch (strategy) {
            case TITLE_ONLY:
                return StringUtils.hasText(question.getTitle()) ? question.getTitle() : "";

            case CONTENT_ONLY:
                if (StringUtils.hasText(question.getContent())) {
                    String plainContent = HtmlTextExtractor.extractPlainTextWithNewlines(question.getContent());
                    return StringUtils.hasText(plainContent) ? plainContent : "";
                }
                return "";

            case TITLE_CONTENT_SIMPLE:
                return combineSimple(question);

            case TITLE_CONTENT_WEIGHTED:
            default:
                return combineWeighted(question);
        }
    }
    
    /**
     * 简单组合策略：标题 + 内容
     */
    private String combineSimple(Question question) {
        StringBuilder combinedText = new StringBuilder();

        // 添加标题
        if (StringUtils.hasText(question.getTitle())) {
            combinedText.append(question.getTitle());
        }

        // 添加内容
        if (StringUtils.hasText(question.getContent())) {
            String plainContent = HtmlTextExtractor.extractPlainTextWithNewlines(question.getContent());
            if (StringUtils.hasText(plainContent)) {
                if (combinedText.length() > 0) {
                    combinedText.append(" ");
                }
                combinedText.append(plainContent);
            }
        }

        return combinedText.length() > 0 ? combinedText.toString() :
               (StringUtils.hasText(question.getTitle()) ? question.getTitle() : "");
    }
    
    /**
     * 加权优化策略：标题权重更高 + 内容长度限制
     * 优化要点：
     * 1. 标题权重更高，重复添加提升重要性
     * 2. 限制内容长度，避免语义稀释
     * 3. 使用更好的分隔符
     */
    private String combineWeighted(Question question) {
        StringBuilder combinedText = new StringBuilder();

        // 添加标题（提高权重，重复一次）
        if (StringUtils.hasText(question.getTitle())) {
            combinedText.append(question.getTitle());
            combinedText.append(". "); // 使用句号分隔，更符合自然语言
            combinedText.append(question.getTitle()); // 重复标题提高权重
        }

        // 添加内容（去除HTML标签并限制长度）
        if (StringUtils.hasText(question.getContent())) {
            String plainContent = HtmlTextExtractor.extractPlainTextWithNewlines(question.getContent());
            if (StringUtils.hasText(plainContent)) {
                // 限制内容长度，避免过长文本稀释语义
                String truncatedContent = truncateText(plainContent, 300);
                if (combinedText.length() > 0) {
                    combinedText.append(". "); // 使用句号分隔
                }
                combinedText.append(truncatedContent);
            }
        }

        // 如果组合文本为空，至少返回标题
        return combinedText.length() > 0 ? combinedText.toString() :
               (StringUtils.hasText(question.getTitle()) ? question.getTitle() : "");
    }
    
    /**
     * 截断文本到指定长度，保持完整性
     *
     * @param text 原始文本
     * @param maxLength 最大长度
     * @return 截断后的文本
     */
    private String truncateText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }

        // 在最大长度附近寻找合适的截断点（句号、问号、感叹号）
        String truncated = text.substring(0, maxLength);
        int lastSentenceEnd = Math.max(
            Math.max(truncated.lastIndexOf('。'), truncated.lastIndexOf('？')),
            Math.max(truncated.lastIndexOf('！'), truncated.lastIndexOf('.'))
        );

        if (lastSentenceEnd > maxLength * 0.7) { // 如果截断点不太靠前
            return text.substring(0, lastSentenceEnd + 1);
        } else {
            return truncated + "...";
        }
    }
    
    /**
     * 处理一批题目的向量化
     *
     * @param questions 题目列表
     * @return 成功处理的数量
     */
    private int processBatchQuestions(List<Question> questions) {
        try {
            // 提取文本内容 - 组合标题和内容
            List<String> texts = questions.stream()
                    .map(this::combineQuestionText)
                    .collect(Collectors.toList());

            // 调用AI服务获取向量
            ServiceAiFeign.BatchEmbeddingRequest request = new ServiceAiFeign.BatchEmbeddingRequest(texts);
            ApiResponse response = serviceAiFeign.getBatchEmbedding(request);

            if (response.getStatus() != 200 || response.getData() == null) {
                log.error("获取向量失败: {}", response.getMessage());
                return 0;
            }

            // 解析向量数据
            @SuppressWarnings("unchecked")
            List<List<Object>> rawVectors = (List<List<Object>>) response.getData();

            // 将Double转换为Float
            List<List<Float>> vectors = rawVectors.stream()
                    .map(vector -> vector.stream()
                            .map(value -> {
                                if (value instanceof Double) {
                                    return ((Double) value).floatValue();
                                } else if (value instanceof Float) {
                                    return (Float) value;
                                } else {
                                    return Float.valueOf(value.toString());
                                }
                            })
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            if (vectors.size() != questions.size()) {
                log.error("向量数量与题目数量不匹配: {} vs {}", vectors.size(), questions.size());
                return 0;
            }

            // 准备向量数据
            List<QdrantVectorService.VectorData> vectorDataList = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                List<Float> vector = vectors.get(i);

                QdrantVectorService.VectorData vectorData = new QdrantVectorService.VectorData(
                        question.getId().toString(),
                        question.getTitle(),
                        vector
                );
                vectorDataList.add(vectorData);
            }

            // 批量保存到向量数据库
            int savedCount = qdrantVectorService.batchInsertVectors(vectorDataList);

            if (savedCount == questions.size()) {
                log.debug("成功处理 {} 个题目的向量化", savedCount);
                return savedCount;
            } else {
                log.warn("部分题目向量化失败，期望: {}，实际: {}", questions.size(), savedCount);
                return savedCount;
            }

        } catch (Exception e) {
            log.error("处理批次题目向量化失败", e);
            return 0;
        }
    }

    @Override
    public List<QdrantVectorService.SimilarityResult> searchSimilarQuestions(String queryText) {
        try {
            log.info("开始相似度查询，查询文本: {}", queryText);

            // 参数验证
            if (!StringUtils.hasText(queryText)) {
                log.warn("查询文本为空");
                return new ArrayList<>();
            }

            // 调用AI服务获取查询文本的向量
            ServiceAiFeign.SingleEmbeddingRequest request = new ServiceAiFeign.SingleEmbeddingRequest(queryText);
            ApiResponse response = serviceAiFeign.getSingleEmbedding(request);

            if (response.getStatus() != 200 || response.getData() == null) {
                log.error("获取查询文本向量失败: {}", response.getMessage());
                return new ArrayList<>();
            }

            // 解析向量数据
            @SuppressWarnings("unchecked")
            List<List<Object>> rawVectors = (List<List<Object>>) response.getData();

            if (rawVectors.isEmpty()) {
                log.warn("获取到的向量为空");
                return new ArrayList<>();
            }

            // 将Double转换为Float
            List<Float> queryVector = rawVectors.get(0).stream()
                    .map(value -> {
                        if (value instanceof Double) {
                            return ((Double) value).floatValue();
                        } else if (value instanceof Float) {
                            return (Float) value;
                        } else {
                            return Float.valueOf(value.toString());
                        }
                    })
                    .collect(Collectors.toList());

            // 在向量数据库中查询相似内容
            List<QdrantVectorService.SimilarityResult> results = qdrantVectorService.searchSimilarVectors(queryVector);

            log.info("相似度查询完成，找到 {} 个相似题目", results.size());
            return results;

        } catch (Exception e) {
            log.error("相似度查询失败", e);
            return new ArrayList<>();
        }
    }

    // 向量化策略枚举
    public enum VectorizeStrategy {
        TITLE_ONLY,           // 仅使用标题
        CONTENT_ONLY,         // 仅使用内容
        TITLE_CONTENT_SIMPLE, // 标题+内容（简单组合）
        TITLE_CONTENT_WEIGHTED // 标题+内容（加权优化，当前默认策略）
    }
}