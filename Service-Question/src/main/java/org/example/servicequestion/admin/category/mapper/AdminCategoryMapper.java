/**
 * File Name: AdminCategoryMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-05
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.admin.category.dto.ProjectOptionDTO;
import org.example.servicequestion.entity.QuestionCategory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

@Mapper
public interface AdminCategoryMapper extends BaseMapper<QuestionCategory> {

    /**
     * 获取所有分类列表
     */
    @Select("""
                SELECT * FROM question_category 
                LIMIT #{offset}, #{limit}
            """)
    List<QuestionCategory> selectAllList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 按关键词搜索分类列表
     */
    @Select("""
                SELECT * FROM question_category 
                WHERE name LIKE CONCAT('%',#{keyword},'%') OR description LIKE CONCAT('%',#{keyword},'%')
                ORDER BY sort_order ASC, id DESC 
                LIMIT #{offset}, #{limit}
            """)
    List<QuestionCategory> selectListByKeyword(
            @Param("keyword") String keyword,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 按项目ID查询分类列表
     */
    @Select("""
                SELECT * FROM question_category 
                WHERE project_id = #{projectId}
                ORDER BY sort_order ASC, id DESC 
                LIMIT #{offset}, #{limit}
            """)
    List<QuestionCategory> selectListByProjectId(
            @Param("projectId") Long projectId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 按状态查询分类列表
     */
    @Select("""
                SELECT * FROM question_category 
                WHERE status = #{status}
                ORDER BY sort_order ASC, id DESC 
                LIMIT #{offset}, #{limit}
            """)
    List<QuestionCategory> selectListByStatus(
            @Param("status") Integer status,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 获取总数
     */
    @Select("SELECT COUNT(*) FROM question_category")
    Long selectTotalCount();

    /**
     * 按关键词获取总数
     */
    @Select("""
                SELECT COUNT(*) FROM question_category 
                WHERE name LIKE CONCAT('%',#{keyword},'%') OR description LIKE CONCAT('%',#{keyword},'%')
            """)
    Long selectCountByKeyword(@Param("keyword") String keyword);

    /**
     * 按项目ID获取总数
     */
    @Select("SELECT COUNT(*) FROM question_category WHERE project_id = #{projectId}")
    Long selectCountByProjectId(@Param("projectId") Long projectId);

    /**
     * 按状态获取总数
     */
    @Select("SELECT COUNT(*) FROM question_category WHERE status = #{status}")
    Long selectCountByStatus(@Param("status") Integer status);

    /**
     * 获取所有正常状态的项目选项
     */
    @Select("""
        SELECT id, name, description
        FROM project_category
        WHERE status = 1
        ORDER BY sort_order ASC, id DESC
    """)
    List<ProjectOptionDTO> selectProjectOptions();

    }
