package org.example.servicequestion.user.questionBank.service.Impl;


import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.user.questionBank.mapper.QuestionBankCategoryMapper;
import org.example.servicequestion.user.questionBank.mapper.QuestionBankProjectMapper;
import org.example.servicequestion.user.questionBank.mapper.QuestionBankQuestionMapper;
import org.example.servicequestion.user.questionBank.service.GetQuestionBankService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * 题库查询服务实现类
 * 实现与题库相关的所有查询操作
 */
@Service
public class GetQuestionBankServiceImpl implements GetQuestionBankService {

    // Redis键前缀，用于存储题目虚拟ID的映射关系
    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";

    // Redis键前缀，用于存储分类图片URL的映射关系
    private final static String IMG_URL_KEY = RedisConfig.getKey() + "Category:ImgUrl:";


    private final RedisTemplate<String, Object> redisTemplate;
    private final QuestionBankCategoryMapper questionBankCategoryMapper;
    private final QuestionBankProjectMapper questionBankProjectMapper;
    private final QuestionBankQuestionMapper questionBankQuestionMapper;
    private final ServiceImageFeign serviceImageFeign;

    /**
     * 构造函数注入依赖的服务
     *
     * @param redisTemplate              Redis模板
     * @param questionBankCategoryMapper 题库分类Mapper
     * @param questionBankProjectMapper  项目Mapper
     * @param questionBankQuestionMapper 题目Mapper
     * @param serviceImageFeign          图片服务Feign客户端
     */
    @Autowired
    public GetQuestionBankServiceImpl(
            RedisTemplate<String, Object> redisTemplate,
            QuestionBankCategoryMapper questionBankCategoryMapper,
            QuestionBankProjectMapper questionBankProjectMapper,
            QuestionBankQuestionMapper questionBankQuestionMapper,
            ServiceImageFeign serviceImageFeign) {
        this.redisTemplate = redisTemplate;
        this.questionBankCategoryMapper = questionBankCategoryMapper;
        this.questionBankProjectMapper = questionBankProjectMapper;
        this.questionBankQuestionMapper = questionBankQuestionMapper;
        this.serviceImageFeign = serviceImageFeign;
    }


    /**
     * 根据项目ID获取分类列表
     *
     * @param projectId 项目ID
     * @return 分类列表
     */
    @Override
    public List<QuestionCategory> getCategoriesByProjectId(Long projectId) {
        List<QuestionCategory> categories = questionBankCategoryMapper.findCategoriesByProjectId(projectId);
        // 处理分类图片URL
        return processCategoriesImageUrl(categories);
    }

    /**
     * 分页获取项目下的分类列表
     *
     * @param projectId 项目ID
     * @param page      页码，从1开始
     * @param pageSize  每页大小
     * @return 包含分页数据和总数的Map，包含'total'和'list'两个键
     */
    @Override
    public Map<String, Object> getCategoriesByProjectIdPaged(Long projectId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 获取分页数据
        List<QuestionCategory> categories = questionBankCategoryMapper.findCategoriesByProjectIdWithPaging(
                projectId, offset, pageSize);

        // 处理分类图片URL
        List<QuestionCategory> processedCategories = processCategoriesImageUrl(categories);

        // 获取总数
        int total = questionBankCategoryMapper.countCategoriesByProjectId(projectId);

        result.put("list", processedCategories);
        result.put("total", total);

        return result;
    }

    /**
     * 获取所有项目分类
     *
     * @return 项目分类列表
     */
    @Override
    public List<ProjectCategory> getProjects() {
        return questionBankProjectMapper.findAllProjects();
    }

    /**
     * 获取指定分类下的题目数量
     *
     * @param categoryId 分类ID
     * @return 题目数量
     */
    @Override
    public int getCountByCategoryId(Long categoryId) {
        return questionBankQuestionMapper.findCountByCategoryId(categoryId);
    }

    /**
     * 根据分类ID获取分类详情
     *
     * @param categoryId 分类ID
     * @return 分类详情对象
     */
    @Override
    public QuestionCategory getCategoryById(Long categoryId) {
        QuestionCategory category = questionBankCategoryMapper.findCategoryById(categoryId);
        if (category != null) {
            List<QuestionCategory> categories = new ArrayList<>();
            categories.add(category);
            List<QuestionCategory> processed = processCategoriesImageUrl(categories);
            return processed.get(0);
        }
        return category;
    }

    /**
     * 处理分类图片URL
     * 如果图片URL以#开头，则从Redis缓存获取或调用远程服务获取真实URL
     *
     * @param categories 需要处理的分类列表
     * @return 处理后的分类列表
     */
    private List<QuestionCategory> processCategoriesImageUrl(List<QuestionCategory> categories) {
        // 如果分类列表为空，直接返回
        if (categories == null || categories.isEmpty()) {
            System.out.println("null");
            return categories;
        }

        // 收集需要处理的图片文件名映射（未缓存到 Redis 的图片）
        Map<QuestionCategory, String> categoriesToProcess = new ConcurrentHashMap<>();
        for (QuestionCategory category : categories) {
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

        return categories;
    }

    /**
     * 根据分类ID获取该分类下的所有题目
     * 该方法还会为每个题目生成虚拟ID并存储在Redis中
     *
     * @param categoryId 分类ID
     * @return 带有虚拟ID的题目列表
     */
    @Override
    public List<Question> getQuestionsByCategoryId(Long categoryId) {
        // 1. 获取该分类下的题目列表
        List<Question> questionList = questionBankQuestionMapper.findQuestionsByCategoryId(categoryId);

        // 2. 准备 Redis 键列表，用于获取虚拟ID
        List<String> keysToFetch = new ArrayList<>(questionList.size());
        for (Question question : questionList) {
            keysToFetch.add(VidKey + question.getId()); // Redis 键
        }

        // 3. 从 Redis 批量获取虚拟ID映射
        List<Object> cachedVirtualIds = null;
        if (!keysToFetch.isEmpty()) {
            cachedVirtualIds = redisTemplate.opsForValue().multiGet(keysToFetch);
        }

        // 4. 更新题目列表并生成/获取虚拟ID
        Map<String, String> virtualIdMap = new HashMap<>();
        int startIndex = 1; // 如果你需要重新设定索引，可以定义自己的起始索引
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            Long originalId = question.getId();
            String redisKeyOriginal = keysToFetch.get(i);

            // 获取虚拟ID
            String virtualId = cachedVirtualIds != null && i < cachedVirtualIds.size() ?
                    (String) cachedVirtualIds.get(i) : null;

            if (virtualId == null) {
                // 如果 Redis 中没有虚拟ID，则生成新的虚拟ID
                virtualId = IdEncryptor.encryptId(originalId); // 使用 IdEncryptor 生成虚拟ID

                // 将虚拟ID和原ID的映射存入 Redis
                virtualIdMap.put(redisKeyOriginal, virtualId);
                virtualIdMap.put(VidKey + virtualId, String.valueOf(originalId)); // 双向映射
            }

            // 设置虚拟ID和更新索引
            question.setVirtualId(virtualId); // 设置虚拟ID
            question.setId((long) startIndex++); // 更新题目ID作为索引
        }

        // 5. 批量存储虚拟ID到 Redis
        if (!virtualIdMap.isEmpty()) {
            redisTemplate.opsForValue().multiSet(virtualIdMap); // 批量存储虚拟ID映射
        }

        return questionList; // 返回带有虚拟ID的题目列表
    }
}
