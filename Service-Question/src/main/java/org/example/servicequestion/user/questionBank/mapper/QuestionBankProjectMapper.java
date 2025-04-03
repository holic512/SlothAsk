package org.example.servicequestion.user.questionBank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.ProjectCategory;

import java.util.List;

@Mapper
public interface QuestionBankProjectMapper extends BaseMapper<QuestionBankProjectMapper> {

    // 查询所有项目
    @Select("SELECT * FROM project_category "+
            "ORDER BY sort_order ASC, create_time DESC")
    List<ProjectCategory> findAllProjects();
}
