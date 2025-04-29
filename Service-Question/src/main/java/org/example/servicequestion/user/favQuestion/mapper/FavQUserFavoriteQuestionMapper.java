/**
 * File Name: FavQUserFavoriteQuestionMapper.java
 * Description: 用户题目收藏数据库操作接口，提供批量插入、更新、删除与查询收藏题目记录的能力。
 * Author: holic512
 * Created Date: 2025-04-28
 * Version: 1.0
 * Usage:
 * 由 MyBatis 注解实现具体 SQL 操作，支持批量收藏、取消收藏、根据题目标题模糊查询收藏题目等功能。
 */

package org.example.servicequestion.user.favQuestion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.servicequestion.entity.UserFavoriteQuestion;
import org.example.servicequestion.user.favQuestion.dto.FavQuestionDTO;

import java.util.List;

@Mapper
public interface FavQUserFavoriteQuestionMapper extends BaseMapper<UserFavoriteQuestion> {

    /**
     * 批量插入收藏记录
     *
     * @param favoriteQuestions 收藏记录列表
     * @return 插入成功的记录数
     */
    @Insert({
            "<script>",
            "INSERT INTO user_favorite_question (id, user_id, question_id, create_time)",
            "VALUES",
            "<foreach collection='favoriteQuestions' item='item' separator=','>",
            "  (#{item.id}, #{item.userId}, #{item.questionId}, #{item.createTime})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(@Param("favoriteQuestions") List<UserFavoriteQuestion> favoriteQuestions);


    /**
     * 批量插入收藏记录，当(user_id, question_id)重复时更新create_time
     *
     * @param favoriteQuestions 收藏记录列表
     * @return 插入/更新的记录数
     */
    @Insert({
            "<script>",
            "INSERT INTO user_favorite_question (user_id, question_id, create_time)",
            "VALUES",
            "<foreach collection='favoriteQuestions' item='item' separator=','>",
            "  (#{item.userId}, #{item.questionId}, #{item.createTime})",
            "</foreach>",
            "ON DUPLICATE KEY UPDATE create_time = VALUES(create_time)",
            "</script>"
    })
    int batchInsertOrUpdate(@Param("favoriteQuestions") List<UserFavoriteQuestion> favoriteQuestions);

    /**
     * 批量删除收藏记录(基于user_id和question_id组合条件)
     *
     * @param favoriteQuestions 要删除的收藏记录列表
     * @return 删除的行数
     */
    @Delete({
            "<script>",
            "DELETE FROM user_favorite_question",
            "WHERE (user_id, question_id) IN",
            "<foreach collection='favoriteQuestions' item='item' open='(' separator=',' close=')'>",
            "  (#{item.userId}, #{item.questionId})",
            "</foreach>",
            "</script>"
    })
    int batchDeleteByUserAndQuestion(@Param("favoriteQuestions") List<UserFavoriteQuestion> favoriteQuestions);

    /**
     * 查询用户收藏的题目列表
     *
     * @param userId 用户ID
     * @return 收藏题目列表
     */
    @Select("SELECT * FROM user_favorite_question WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<UserFavoriteQuestion> selectUserFavQuestions(@Param("userId") Long userId);
    
    /**
     * 查询用户收藏的题目详情，包含题目标题
     *
     * @param userId 用户ID
     * @return 收藏题目详情列表
     */
    @Select("SELECT ufq.question_id, q.title, ufq.create_time " +
            "FROM user_favorite_question ufq " +
            "LEFT JOIN question q ON ufq.question_id = q.id " +
            "WHERE ufq.user_id = #{userId} " +
            "ORDER BY ufq.create_time DESC")
    List<FavQuestionDTO> selectUserFavQuestionsDetail(@Param("userId") Long userId);
    
    /**
     * 根据题目标题搜索用户收藏的题目
     *
     * @param userId 用户ID
     * @param title 题目标题（模糊搜索）
     * @return 匹配的收藏题目详情列表
     */
    @Select("SELECT ufq.question_id, q.title, ufq.create_time " +
            "FROM user_favorite_question ufq " +
            "LEFT JOIN question q ON ufq.question_id = q.id " +
            "WHERE ufq.user_id = #{userId} " +
            "AND q.title LIKE CONCAT('%', #{title}, '%') " +
            "ORDER BY ufq.create_time DESC")
    List<FavQuestionDTO> searchUserFavQuestionsByTitle(@Param("userId") Long userId, @Param("title") String title);
    
    /**
     * 检查用户是否收藏了特定题目
     *
     * @param userId     用户ID
     * @param questionId 题目ID
     * @return 匹配的记录数
     */
    @Select("SELECT COUNT(*) FROM user_favorite_question " +
            "WHERE user_id = #{userId} AND question_id = #{questionId}")
    Integer checkUserFavorite(@Param("userId") Long userId, @Param("questionId") Long questionId);
}
