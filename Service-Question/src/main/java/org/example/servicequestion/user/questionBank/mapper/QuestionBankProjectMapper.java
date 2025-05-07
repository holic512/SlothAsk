package org.example.servicequestion.user.questionBank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.ProjectCategory;

import java.util.List;

/**
 * 项目分类数据访问接口
 * 负责项目分类数据的查询操作
 */
@Mapper
public interface QuestionBankProjectMapper extends BaseMapper<QuestionBankProjectMapper> {

    /**
     * 查询所有项目分类
     * 按排序字段和创建时间排序返回所有项目
     * 
     * @return 项目分类列表
     */
    @Select("SELECT * FROM project_category "+
            "ORDER BY sort_order ASC, create_time DESC")
    List<ProjectCategory> findAllProjects();
}
