/**
 * File Name: QuestionCategoryMapper.java
 * Description: 题库分类的数据库操作接口
 * Author: holic512
 * Created Date: 2024-03-21
 * Version: 1.0
 */
package org.example.servicequestion.user.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.servicequestion.entity.QuestionCategory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface QuestionCategoryMapper extends BaseMapper<QuestionCategory> {
    
    /**
     * 获取推荐的题库分类（无项目ID限制）
     * 按照排序、访问量、ID的优先级获取前6个分类
     * @return 推荐的题库分类列表
     */
    @Select("SELECT id, name, description, creator_id, avatar_url, sort_order, view_count, status, create_time, update_time, project_id " +
            "FROM question_category " +
            "WHERE status = 1 " +
            "ORDER BY sort_order, view_count DESC, id ASC " +
            "LIMIT 6")
    List<QuestionCategory> findRecommendedCategories();
    
    /**
     * 获取指定项目的推荐题库分类
     * 按照排序、访问量、ID的优先级获取前6个分类
     * @param projectId 项目ID
     * @return 推荐的题库分类列表
     */
    @Select("SELECT id, name, description, creator_id, avatar_url, sort_order, view_count, status, create_time, update_time, project_id " +
            "FROM question_category " +
            "WHERE project_id = #{projectId} AND status = 1 " +
            "ORDER BY sort_order, view_count DESC, id ASC " +
            "LIMIT 6")
    List<QuestionCategory> findRecommendedCategoriesByProjectId(@Param("projectId") Long projectId);
    
    /**
     * 增加分类的访问量
     * @param categoryId 分类ID
     * @return 更新的行数
     */
    @Update("UPDATE question_category SET view_count = view_count + 1 WHERE id = #{categoryId}")
    int incrementViewCount(@Param("categoryId") Long categoryId);
} 