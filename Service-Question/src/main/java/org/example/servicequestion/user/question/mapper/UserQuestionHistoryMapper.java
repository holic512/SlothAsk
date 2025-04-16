package org.example.servicequestion.user.question.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.UserQuestionHistory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户访问问题历史记录Mapper接口(简化版)
 */
@Mapper
public interface UserQuestionHistoryMapper extends BaseMapper<UserQuestionHistory> {
    
    /**
     * 获取用户最近访问的问题ID列表
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 问题ID列表
     */
    @Select("SELECT question_id FROM user_question_history WHERE user_id = #{userId} " +
            "ORDER BY visit_time DESC LIMIT #{limit}")
    List<Long> findRecentVisitedQuestionIds(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 删除用户已存在的问题访问记录
     * 确保每个用户对每个问题只保留一条最新记录
     *
     * @param userId     用户ID
     * @param questionId 问题ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM user_question_history WHERE user_id = #{userId} AND question_id = #{questionId}")
    int deleteExistingRecord(@Param("userId") Long userId, @Param("questionId") Long questionId);
    
    /**
     * 插入或更新用户问题访问记录
     * 如果记录存在则更新访问时间，不存在则插入新记录
     * 使用MySQL的INSERT ON DUPLICATE KEY UPDATE语法实现高效插入
     *
     * @param userId     用户ID
     * @param questionId 问题ID
     * @param visitTime  访问时间
     * @return 影响的行数
     */
    @Insert("INSERT INTO user_question_history(user_id, question_id, visit_time) " +
            "VALUES(#{userId}, #{questionId}, #{visitTime}) " +
            "ON DUPLICATE KEY UPDATE visit_time = #{visitTime}")
    int insertOrUpdateVisitRecord(@Param("userId") Long userId, 
                               @Param("questionId") Long questionId, 
                               @Param("visitTime") LocalDateTime visitTime);
} 