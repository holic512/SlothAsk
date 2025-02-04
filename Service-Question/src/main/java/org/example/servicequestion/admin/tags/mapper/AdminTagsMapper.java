/**
 * File Name: AdminTagsMapper.java
 * Description: 标签管理系统的数据访问层，提供标签相关的数据库操作接口
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   提供以下数据库操作功能：
 *   1. 项目分类的查询
 *   2. 标签的增删改查
 *   3. 支持分页和关键字搜索
 *   4. 批量操作支持
 * 
 * 主要功能：
 * 1. 查询项目分类列表
 * 2. 标签的分页查询（支持关键字搜索）
 * 3. 标签的删除（单个/批量）
 * 4. 标签信息的更新
 */
package org.example.servicequestion.admin.tags.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.servicequestion.admin.tags.dto.GetProjectListDto;
import org.example.servicequestion.admin.tags.dto.GetTagsListDto;
import org.example.servicequestion.entity.QuestionTagCategory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface AdminTagsMapper extends BaseMapper<QuestionTagCategory> {
    /**
     * 查询所有正常状态的项目分类列表
     *
     * @return 项目分类DTO列表
     */
    @Select("SELECT id, name, description " +
            "FROM project_category " +
            "WHERE status = 1 " +
            "ORDER BY sort_order ")
    List<GetProjectListDto> selectActiveProjectList();

    /**
     * 查询所有标签列表（不带关键字搜索）
     */
    @Select("SELECT t.id, t.project_id, t.name, t.description, t.sort_order, t.status, t.update_time " +
            "FROM question_tag_category t " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetTagsListDto> selectTagsList(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询标签列表（带关键字搜索）
     */
    @Select("SELECT t.id, t.project_id, t.name, t.description, t.sort_order, t.status, t.update_time " +
            "FROM question_tag_category t " +
            "WHERE t.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR t.description LIKE CONCAT('%', #{keyword}, '%') " +
            "LIMIT #{offset}, #{pageSize}")
    List<GetTagsListDto> selectTagsListWithKeyword(@Param("keyword") String keyword, 
                                                  @Param("offset") int offset, 
                                                  @Param("pageSize") int pageSize);

    /**
     * 获取总记录数（不带关键字）
     */
    @Select("SELECT COUNT(*) FROM question_tag_category")
    Long selectTotalCount();

    /**
     * 获取总记录数（带关键字）
     */
    @Select("SELECT COUNT(*) FROM question_tag_category t " +
            "WHERE t.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR t.description LIKE CONCAT('%', #{keyword}, '%')")
    Long selectTotalCountWithKeyword(@Param("keyword") String keyword);

    /**
     * 删除单个标签
     * @param id 标签ID
     * @return 影响行数
     */
    @Delete("DELETE FROM question_tag_category WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除标签
     * @param ids 标签ID列表
     * @return 影响行数
     */
    @Delete("<script>" +
            "DELETE FROM question_tag_category WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")

    int deleteBatchIds(@Param("ids") List<Long> ids);

    /**
     * 更新标签信息
     */
    @Update("UPDATE question_tag_category " +
            "SET project_id = #{projectId}, " +
            "    name = #{name}, " +
            "    description = #{description}, " +
            "    sort_order = #{sortOrder}, " +
            "    status = #{status}, " +
            "    update_time = NOW() " +
            "WHERE id = #{id}")
    int updateTag(@Param("id") Long id,
                 @Param("projectId") Long projectId,
                 @Param("name") String name,
                 @Param("description") String description,
                 @Param("sortOrder") Integer sortOrder,
                 @Param("status") Integer status);
}
