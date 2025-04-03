package org.example.servicequestion.user.questionBank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;

import java.util.List;


@Mapper
public interface QuestionBankQuestionMapper extends BaseMapper<Question> {

    // 查询题目数量
    @Select("SELECT count(*) FROM question WHERE category_id = #{categoryId} AND status = 1")
    int findCountByCategoryId(@Param("categoryId") Long categoryId);

    // 查询某分类下的题目
    @Select("SELECT * FROM question WHERE category_id = #{categoryId} AND status = 1")
    List<Question> findQuestionsByCategoryId(@Param("categoryId") Long categoryId);

    // 查询单个题目（添加此方法）
    @Select("SELECT * FROM question WHERE id = #{id} AND status = 1")
    Question findQuestionById(@Param("id") Long id);
}

