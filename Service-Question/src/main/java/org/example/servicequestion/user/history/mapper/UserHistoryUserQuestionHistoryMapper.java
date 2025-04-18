/**
 * File Name: UserHistoryUserQuestionHistoryMapper.java
 * Description: 用户问题历史Mapper接口
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.UserQuestionHistory;
import org.example.servicequestion.user.history.dto.UserHistoryDTO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserHistoryUserQuestionHistoryMapper extends BaseMapper<UserQuestionHistory> {

    /**
     * 获取用户的历史问题列表
     *
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 用户历史问题信息列表
     */
    @Select("SELECT h.question_id AS virtualId, q.title, q.difficulty, c.name AS categoryName, h.visit_time,q.tag_category_ids AS tagCategoryIds " +
            "FROM user_question_history h " +
            "JOIN question q ON h.question_id = q.id " +
            "JOIN question_category c ON q.category_id = c.id " +
            "WHERE h.user_id = #{userId} " +
            "ORDER BY h.visit_time DESC " +
            "LIMIT #{offset}, #{limit}")
    List<UserHistoryDTO> getUserHistoryList(@Param("userId") Long userId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    /**
     * 删除单条历史记录
     *
     * @param userId   用户ID
     * @param questionId 问题ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_question_history WHERE question_id = #{questionId} and user_id = #{userId}")
    int deleteHistoryRecord(@Param("userId") Long userId, @Param("questionId") String questionId);

    /**
     * 清空指定日期的历史记录
     *
     * @param userId 用户ID
     * @param date 日期 (格式应为"yyyy-MM-dd")
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_question_history " +
            "WHERE user_id = #{userId} " +
            "AND DATE(visit_time) = #{date}")
    int clearDayHistoryRecords(@Param("userId") Long userId, @Param("date") String date);

}
