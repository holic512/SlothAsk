/**
 * File Name: UserQuestionCategoryMapper.java
 * Description: 用户问题分类Mapper接口
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.QuestionCategory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


@Mapper
public interface UserQuestionCategoryMapper extends BaseMapper<QuestionCategory> {
    
    /**
     * 根据分类ID查询分类名称
     * 
     * @param categoryId 分类ID
     * @return 分类名称
     */
    @Select("SELECT name FROM question_category WHERE id = #{categoryId}")
    String selectNameById(Long categoryId);
} 