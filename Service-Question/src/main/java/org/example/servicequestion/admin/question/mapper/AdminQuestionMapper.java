/**
 * File Name: AdminQuestionMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.admin.question.dto.GetCateDto;
import org.example.servicequestion.admin.question.dto.GetProjectDto;
import org.example.servicequestion.admin.question.dto.GetTagDto;
import org.example.servicequestion.entity.Question;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface AdminQuestionMapper extends BaseMapper<Question> {

    /**
     * 获取项目列表
     */
    @Select("SELECT p.id, p.name FROM project_category p ")
    List<GetProjectDto> selectProjectList();

    /**
     * 获取项目id下的 分类列表
     */
    @Select("SELECT c.id, c.name FROM question_category c where project_id = #{ProjectId}")
    List<GetCateDto> selectCateListByProjectId(@Param("ProjectId") Long ProjectId);


    /**
     * 获取项目id下的 标签列表
     */
    @Select("SELECT c.id, c.name FROM slothAsk.question_tag_category c where project_id = #{ProjectId}")
    List<GetTagDto> selectTagListByProjectId(@Param("ProjectId") Long ProjectId);

    /**
     * 获取全部分类列表
     */
    @Select("SELECT c.id, c.name FROM question_category c")
    List<GetCateDto> selectAllCateList();

    /**
     * 获取全部的标签列表
     */
    @Select("SELECT c.id, c.name FROM slothAsk.question_tag_category c")
    List<GetTagDto> selectAllTagList();

    int deleteById(Long id);


    @Delete("<script>" +
            "DELETE FROM question " +
            "WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteBatchIds(@Param("ids") List<Long> ids);

}
