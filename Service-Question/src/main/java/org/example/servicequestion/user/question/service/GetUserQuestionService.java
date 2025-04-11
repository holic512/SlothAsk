/**
 * File Name: GetUserQuestionService.java
 * Description: 获取用户问题服务接口
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.service;

import org.example.servicequestion.user.question.dto.CommentGetDTO;
import org.example.servicequestion.user.question.dto.QuestionDTO;
import org.example.servicequestion.user.question.dto.QuestionListDTO;

import java.util.List;

public interface GetUserQuestionService {

    /**
     * 根据虚拟ID获取题目信息(不包含答案)
     *
     * @param virtualId 虚拟题目ID
     * @return 题目信息DTO
     */
    QuestionDTO getQuestionByVirtualId(String virtualId);

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
     * @param
     */
    void addComment(String questionId,Long userId,String content,Long parentId);


    /**
     * 更新评论点赞数
     *
     * @param commentId 评论ID
     */
    void updateLikeCount(Long commentId);

}
