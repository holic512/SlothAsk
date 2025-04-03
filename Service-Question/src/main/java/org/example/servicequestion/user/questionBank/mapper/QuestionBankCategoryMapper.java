package org.example.servicequestion.user.questionBank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.QuestionCategory;

import java.util.List;

@Mapper
public interface QuestionBankCategoryMapper extends BaseMapper<QuestionBankCategoryMapper> {

    // 根据项目id查询分类
    @Select("SELECT * FROM question_category " +
            "WHERE project_id = #{projectId} AND status = 1 " +
            "ORDER BY sort_order ASC, create_time DESC")
    List<QuestionCategory> findCategoriesByProjectId(@Param("projectId") long projectId);

    // 根据分类id查询分类
    @Select("SELECT * From question_category "+
            "WHERE id = #{categoryId} AND status = 1 ")
    QuestionCategory findCategoryById(@Param("categoryId")  Long categoryId);
}
