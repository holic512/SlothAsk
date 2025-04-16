/**
 * File Name: GetUserQuestionService.java
 * Description: 获取用户问题服务接口
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.service;

import java.util.List;

import org.example.servicequestion.user.question.dto.CommentGetDTO;
import org.example.servicequestion.user.question.dto.QuestionDTO;
import org.example.servicequestion.user.question.dto.QuestionListDTO;
import org.example.servicequestion.user.question.dto.QuestionResponseDTO;

public interface GetUserQuestionService {

    /**
     * 根据虚拟ID获取原始ID
     *
     * @param virtualId 虚拟ID
     * @return 原始ID
     */
    Long getOriginalIdFromVirtualId(String virtualId);

    /**
     * 根据虚拟ID获取题目信息(不包含答案)
     *
     * @param virtualId 虚拟ID
     * @return 题目信息
     */
    QuestionDTO getQuestionByVirtualId(String virtualId);

    /**
     * 根据虚拟ID获取题目信息和真实ID(内部服务使用)
     * 真实ID仅供内部服务调用，不暴露给前端
     *
     * @param virtualId 虚拟ID
     * @return 封装了题目信息和真实ID的响应对象
     */
    QuestionResponseDTO getQuestionWithRealId(String virtualId);

    /**
     * 根据虚拟ID获取题目答案
     *
     * @param virtualId 虚拟题目ID
     * @return 题目答案
     */
    String getQuestionAnswerByVirtualId(String virtualId);

    /**
     * 根据虚拟ID获取同分类下的题目列表，并确保当前题目在返回的列表中
     * 
     * @param virtualId 虚拟题目ID
     * @param page 请求的页码（从1开始）
     * @return 题目列表DTO，包含当前页题目和分页信息
     */
    QuestionListDTO getCategoryQuestionsByVirtualId(String virtualId, int page);

    /**
     * 根据虚拟ID获取同分类下的评论
     *
     * @param virtualId 虚拟题目ID
     * @return 评论列表
     */

    List<CommentGetDTO> getCommentsByVirtualId(String virtualId);

    /**
     * 添加评论
     *
     * @param questionId 题目ID
     * @param userId 用户ID
     * @param content 评论内容
     * @param parentId 父评论ID
     */
    void addComment(String questionId, Long userId, String content, Long parentId);


    /**
     * 更新评论点赞数
     *
     * @param commentId 评论ID
     */
    void updateLikeCount(Long commentId);

    /**
     * 记录题目访问量
     * 
     * @param virtualId 虚拟ID
     * @param userId 用户ID，可以是IP地址或用户唯一标识
     */
    void recordQuestionView(String virtualId, String userId);

}
