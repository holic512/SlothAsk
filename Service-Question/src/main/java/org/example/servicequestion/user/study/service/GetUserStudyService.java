/**
 * File Name: GetUserCategoryService.java
 * Description: 获取用户题库分类的服务接口
 * Author: holic512
 * Created Date: 2025-03-19
 * Version: 1.0
 * Usage:
 * 提供用户题库分类相关的业务逻辑服务,包括:
 * - 获取推荐分类
 * - 获取分类列表
 * - 获取标签列表
 * - 获取题目列表
 */
package org.example.servicequestion.user.study.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.user.study.dto.CategoryIdAndNameDto;
import org.example.servicequestion.user.study.dto.HotQuestionDto;
import org.example.servicequestion.user.study.dto.TagIdAndNameDto;
import org.example.servicequestion.user.study.dto.UserSubmitCountDto;
import org.example.servicequestion.user.study.request.GetQuestionListRequest;

import java.util.List;

/**
 * 用户题库分类服务接口
 * 定义了题库分类相关的核心业务逻辑
 */
public interface GetUserStudyService {
    /**
     * 获取推荐的题库分类
     * 根据项目ID获取推荐分类列表,优先级为:排序、访问量、ID
     * 
     * @param projectId 项目ID,为null时获取全部分类中的推荐
     * @return 推荐的题库分类列表
     */
    List<QuestionCategory> getRecommendedCategories(Long projectId);

    /**
     * 获取项目下所有分类的ID和名称
     * 用于前端展示分类选择列表
     *
     * @param projectId 项目ID,为null时默认获取项目ID为1的分类
     * @return 分类ID和名称列表
     */
    List<CategoryIdAndNameDto> getCategoryIdAndName(Long projectId);

    /**
     * 获取项目下所有标签的ID和名称
     * 用于前端展示标签选择列表
     *
     * @param projectId 项目ID,为null时默认获取项目ID为1的标签
     * @return 标签ID和名称列表
     */
    List<TagIdAndNameDto> getTagIdAndName(Long projectId);

    /**
     * 获取项目下符合过滤条件的题目列表
     * 支持多种过滤条件:
     * - 关键词搜索(标题和内容)
     * - 分类过滤
     * - 标签过滤
     * - 类型过滤
     * - 难度过滤
     * 
     * @param projectId 项目ID
     * @param request 包含过滤条件的请求对象
     * @return 分页的题目列表
     */
    Page<Question> getQuestionList(Long projectId, GetQuestionListRequest request);

    /**
     * 获取下一题的虚拟ID
     * 根据当前题目的虚拟ID,查找同一分类下的下一题,并返回其虚拟ID
     * 
     * @param currentVid 当前题目的虚拟ID
     * @return 下一题的虚拟ID,如果没有下一题则返回null
     */
    String getNextQuestionVid(String currentVid);

    /**
     * 获取用户提交次数统计
     * 查询Redis中当天该用户的提交次数缓存数据，同时从数据库中查出该用户最近89天的提交记录
     * 
     * @param userId 用户ID
     * @return 用户提交次数统计列表，包含时间和次数
     */
    List<UserSubmitCountDto> getUserSubmitCountStats(Long userId);

    /**
     * 获取浏览量最高的前10道题目
     * 根据项目ID获取热门题目列表，按浏览量降序排列
     * 
     * @param projectId 项目ID，为null时获取全部项目的热门题目
     * @return 热门题目列表，包含虚拟ID、标题和浏览量
     */
    List<HotQuestionDto> getHotQuestions(Long projectId);

}
