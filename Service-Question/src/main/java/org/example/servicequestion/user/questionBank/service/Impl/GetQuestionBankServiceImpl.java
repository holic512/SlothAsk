package org.example.servicequestion.user.questionBank.service.Impl;


import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;
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



@Service
public class GetQuestionBankServiceImpl  implements GetQuestionBankService {

    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";


    private final RedisTemplate<String, Object> redisTemplate;
    private final QuestionBankCategoryMapper questionBankCategoryMapper;
    private final QuestionBankProjectMapper questionBankProjectMapper;
    private final QuestionBankQuestionMapper questionBankQuestionMapper;

    @Autowired
    public GetQuestionBankServiceImpl(
            RedisTemplate<String, Object> redisTemplate,
            QuestionBankCategoryMapper questionBankCategoryMapper,
            QuestionBankProjectMapper questionBankProjectMapper,
            QuestionBankQuestionMapper questionBankQuestionMapper) {
        this.redisTemplate = redisTemplate;
        this.questionBankCategoryMapper = questionBankCategoryMapper;
        this.questionBankProjectMapper = questionBankProjectMapper;
        this.questionBankQuestionMapper = questionBankQuestionMapper;
    }



    @Override
    public List<QuestionCategory> getCategoriesByProjectId(Long projectId) {
        return questionBankCategoryMapper.findCategoriesByProjectId(projectId);
    }

    @Override
    public List<ProjectCategory> getProjects() {
        return questionBankProjectMapper.findAllProjects();
    }

    @Override
    public int getCountByCategoryId(Long categoryId) {
        return questionBankQuestionMapper.findCountByCategoryId(categoryId);
    }

    @Override
    public QuestionCategory getCategoryById(Long categoryId) {
        return questionBankCategoryMapper.findCategoryById(categoryId);
    }

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
