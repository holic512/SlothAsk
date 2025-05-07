package org.example.servicequestion.user.questionBank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.Question;

import java.util.List;

/**
 * 题目数据访问接口
 * 负责题目数据的查询操作
 */
@Mapper
public interface QuestionBankQuestionMapper extends BaseMapper<Question> {

    /**
     * 查询指定分类下的题目数量
     * 只统计状态正常(status=1)的题目
     * 
     * @param categoryId 分类ID
     * @return 题目数量
     */
    @Select("SELECT count(*) FROM question WHERE category_id = #{categoryId} AND status = 1")
    int findCountByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 查询指定分类下的所有题目
     * 只返回状态正常(status=1)的题目
     * 
     * @param categoryId 分类ID
     * @return 题目列表
     */
    @Select("SELECT * FROM question WHERE category_id = #{categoryId} AND status = 1")
    List<Question> findQuestionsByCategoryId(@Param("categoryId") Long categoryId);

}

