/**
 * File Name: UserQuestionQuestionMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.user.question.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.servicequestion.entity.Question;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户题目Mapper接口
 */
@Mapper
public interface UserQuestionQuestionMapper extends BaseMapper<Question> {
    
    /**
     * 统计分类下的题目总数
     * 
     * @param categoryId 分类ID
     * @return 题目总数
     */
    @Select("SELECT COUNT(*) FROM question WHERE category_id = #{categoryId}")
    int countByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 查找题目在分类中的索引位置
     * 
     * @param questionId 题目ID
     * @param categoryId 分类ID
     * @return 索引位置（从0开始）
     */
    @Select("SELECT COUNT(*) FROM question WHERE category_id = #{categoryId} AND id < #{questionId}")
    int findIndexInCategoryById(@Param("questionId") Long questionId, @Param("categoryId") Long categoryId);
    
    /**
     * 分页获取分类下的题目
     * 
     * @param categoryId 分类ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 题目列表
     */
    @Select("SELECT * FROM question WHERE category_id = #{categoryId} ORDER BY id ASC LIMIT #{offset}, #{limit}")
    List<Question> findByCategoryIdOrderByIdAsc(
            @Param("categoryId") Long categoryId, 
            @Param("offset") int offset, 
            @Param("limit") int limit);
            
    /**
     * 增加题目访问量
     * 
     * @param questionId 题目ID
     * @param increment 增加的访问量
     * @return 影响的行数
     */
    @Update("UPDATE question SET view_count = view_count + #{increment} WHERE id = #{questionId}")
    int incrementViewCount(@Param("questionId") Long questionId, @Param("increment") Long increment);
}
