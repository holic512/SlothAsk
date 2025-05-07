package org.example.servicequestion.user.questionBank.service;

import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;

import java.util.List;
import java.util.Map;

/**
 * 题库查询服务接口
 * 定义与题库相关的所有查询操作
 */
public interface GetQuestionBankService {

    /**
     * 根据项目ID获取该项目下的所有题库分类
     * 
     * @param projectId 项目ID
     * @return 题库分类列表
     */
    List<QuestionCategory> getCategoriesByProjectId(Long projectId);
    
    /**
     * 分页获取项目下的分类列表
     *
     * @param projectId 项目ID
     * @param page 页码，从1开始
     * @param pageSize 每页大小
     * @return 包含分页数据和总数的Map，包含'total'和'list'两个键
     */
    Map<String, Object> getCategoriesByProjectIdPaged(Long projectId, int page, int pageSize);

    /**
     * 获取所有项目分类
     * 
     * @return 项目分类列表
     */
    List<ProjectCategory> getProjects();

    /**
     * 获取指定分类下的题目数量
     * 
     * @param categoryId 分类ID
     * @return 题目数量
     */
    int getCountByCategoryId(Long categoryId);

    /**
     * 根据分类ID获取分类详情
     * 
     * @param categoryId 分类ID
     * @return 分类详情对象
     */
    QuestionCategory getCategoryById(Long categoryId);

    /**
     * 根据分类ID获取该分类下的所有题目
     * 返回的题目包含虚拟ID信息
     * 
     * @param categoryId 分类ID
     * @return 题目列表
     */
    List<Question> getQuestionsByCategoryId(Long categoryId);
}
