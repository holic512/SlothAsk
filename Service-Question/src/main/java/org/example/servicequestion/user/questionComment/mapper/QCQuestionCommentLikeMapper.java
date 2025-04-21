/**
 * File Name: QCQuestionCommentLikeMapper.java
 * Description: 问题评论点赞数据访问层，提供对评论点赞数据的操作接口
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 * Usage:
 * - 继承MyBatis-Plus的BaseMapper，提供基本的CRUD操作
 * - 自定义查询：根据用户ID和评论ID列表查询已点赞的评论ID集合
 * - 主要用于检查用户对特定评论的点赞状态
 */
package org.example.servicequestion.user.questionComment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.QuestionCommentLike;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface QCQuestionCommentLikeMapper extends BaseMapper<QuestionCommentLike> {
    @Select({
            "<script>",
            "SELECT comment_id FROM question_comment_like ",
            "WHERE user_id = #{userId} ",
            "AND comment_id IN ",
            "<foreach collection='commentIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<Long> selectLikedCommentIds(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);

}
