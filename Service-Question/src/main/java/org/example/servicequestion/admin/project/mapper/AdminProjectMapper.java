/**
 * File Name: AdminProjectMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-05
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.admin.project.dto.GetProjectListDto;
import org.example.servicequestion.entity.ProjectCategory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface AdminProjectMapper extends BaseMapper<ProjectCategory> {
    /**
     * 按创建时间降序查询项目列表
     */
    @Select("SELECT p.id, p.name, p.description, p.sort_order as sortOrder, " +
            "p.status, p.create_time as createTime, p.update_time as updateTime " +
            "FROM project_category p " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetProjectListDto> selectProjectListDefault(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 按权重升序查询项目列表
     */
    @Select("SELECT p.id, p.name, p.description, p.sort_order as sortOrder, " +
            "p.status, p.create_time as createTime, p.update_time as updateTime " +
            "FROM project_category p " +
            "ORDER BY p.sort_order ASC " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetProjectListDto> selectProjectListSortAsc(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 按权重降序查询项目列表
     */
    @Select("SELECT p.id, p.name, p.description, p.sort_order as sortOrder, " +
            "p.status, p.create_time as createTime, p.update_time as updateTime " +
            "FROM project_category p " +
            "ORDER BY p.sort_order DESC " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetProjectListDto> selectProjectListSortDesc(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 按创建时间降序查询项目列表（带关键字搜索）
     */
    @Select("SELECT p.id, p.name, p.description, p.sort_order as sortOrder, " +
            "p.status, p.create_time as createTime, p.update_time as updateTime " +
            "FROM project_category p " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR p.description LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY p.create_time DESC " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetProjectListDto> selectProjectListWithKeywordDefault(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 按权重升序查询项目列表（带关键字搜索）
     */
    @Select("SELECT p.id, p.name, p.description, p.sort_order as sortOrder, " +
            "p.status, p.create_time as createTime, p.update_time as updateTime " +
            "FROM project_category p " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR p.description LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY p.sort_order ASC " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetProjectListDto> selectProjectListWithKeywordSortAsc(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 按权重降序查询项目列表（带关键字搜索）
     */
    @Select("SELECT p.id, p.name, p.description, p.sort_order as sortOrder, " +
            "p.status, p.create_time as createTime, p.update_time as updateTime " +
            "FROM project_category p " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR p.description LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY p.sort_order DESC " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetProjectListDto> selectProjectListWithKeywordSortDesc(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 获取总记录数（不带关键字）
     */
    @Select("SELECT COUNT(*) FROM project_category")
    Long selectTotalCount();

    /**
     * 获取总记录数（带关键字）
     */
    @Select("SELECT COUNT(*) FROM project_category p " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR p.description LIKE CONCAT('%', #{keyword}, '%')")
    Long selectTotalCountWithKeyword(@Param("keyword") String keyword);
}
