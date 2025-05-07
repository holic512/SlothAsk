package org.example.servicequestion.user.questionBank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.QuestionCategory;

import java.util.List;

/**
 * 题库分类数据访问接口
 * 负责题库分类数据的查询操作
 */
@Mapper
public interface QuestionBankCategoryMapper extends BaseMapper<QuestionBankCategoryMapper> {

    /**
     * 根据项目ID查询该项目下的所有分类
     * 只返回状态正常(status=1)的分类，按排序字段和创建时间排序
     * 
     * @param projectId 项目ID
     * @return 分类列表
     */
    @Select("SELECT * FROM question_category " +
            "WHERE project_id = #{projectId} AND status = 1 " +
            "ORDER BY sort_order ASC, create_time DESC")
    List<QuestionCategory> findCategoriesByProjectId(@Param("projectId") long projectId);
    
    /**
     * 分页查询项目下的分类
     * 只返回状态正常(status=1)的分类，按排序字段和创建时间排序
     * 
     * @param projectId 项目ID
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 分页后的分类列表
     */
    @Select("SELECT * FROM question_category " +
            "WHERE project_id = #{projectId} AND status = 1 " +
            "ORDER BY sort_order ASC, create_time DESC " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<QuestionCategory> findCategoriesByProjectIdWithPaging(
            @Param("projectId") long projectId, 
            @Param("offset") int offset, 
            @Param("pageSize") int pageSize);
    
    /**
     * 获取项目下分类的总数
     * 只统计状态正常(status=1)的分类
     * 
     * @param projectId 项目ID
     * @return 分类总数
     */
    @Select("SELECT COUNT(*) FROM question_category " +
            "WHERE project_id = #{projectId} AND status = 1")
    int countCategoriesByProjectId(@Param("projectId") long projectId);

    /**
     * 根据分类ID查询分类详情
     * 只返回状态正常(status=1)的分类
     * 
     * @param categoryId 分类ID
     * @return 分类详情，如果不存在则返回null
     */
    @Select("SELECT * From question_category "+
            "WHERE id = #{categoryId} AND status = 1 ")
    QuestionCategory findCategoryById(@Param("categoryId")  Long categoryId);
}
