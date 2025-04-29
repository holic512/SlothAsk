/**
 * File Name: GetQuestionCommentService.java
 * Description: 获取问题评论服务接口
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.service;

import org.example.servicequestion.user.questionComment.dto.CommentListResponseDTO;
import org.example.servicequestion.user.questionComment.dto.CommentQueryDTO;

public interface GetQuestionCommentService {
    
    /**
     * 根据虚拟ID获取评论列表
     * @param queryDTO 查询参数
     * @return 评论列表响应DTO
     */
    CommentListResponseDTO getCommentsByVirtualId(CommentQueryDTO queryDTO,Long user_id);
}
