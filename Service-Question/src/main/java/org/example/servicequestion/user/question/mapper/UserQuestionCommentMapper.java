package org.example.servicequestion.user.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.servicequestion.entity.QuestionComment;
import org.example.servicequestion.user.question.dto.CommentGetDTO;

import java.util.List;

@Mapper
public interface UserQuestionCommentMapper extends BaseMapper<QuestionComment> {

    // 获取一级评论（parent_id 为 null）
    @Select("SELECT c.*, u.nickname, u.avatar FROM question_comment c " +
            "JOIN user_profile u ON c.user_id = u.user_id " +
            "WHERE c.question_id = #{questionId} AND c.parent_id IS NULL AND c.status = 1 " +
            "ORDER BY c.create_time ASC")
    List<CommentGetDTO> getTopLevelComments(@Param("questionId") Long questionId);

    // 获取子评论
    @Select("SELECT c.*, u.nickname, u.avatar FROM question_comment c " +
            "JOIN user_profile u ON c.user_id = u.user_id " +
            "WHERE c.parent_id = #{parentId} AND c.status = 1 " +
            "ORDER BY c.create_time ASC")
    List<CommentGetDTO> getReplies(@Param("parentId") Long parentId);

    // 插入评论
    @Insert("INSERT INTO question_comment(question_id, user_id, content, parent_id) " +
            "VALUES(#{questionId}, #{userId}, #{content}, #{parentId, jdbcType=BIGINT})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertComment(QuestionComment comment);

    // 更新评论点赞数
    @Update("UPDATE question_comment SET like_count = like_count + 1 WHERE id = #{id}")
    void updateLikeCount(@Param("id") Long id);
}
