/**
 * File Name: UserQuestionMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.user.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.Question;

import java.util.List;

@Mapper
public interface UserQuestionMapper extends BaseMapper<Question> {

    // 弃用
    @Select("<script>" +
            "select id, title, difficulty, type, tag_category_id, view_count " +
            "from slothAsk.question " +
            "<where>" +
            "<if test='searchText != null and searchText != \"\"'> " +
            "AND title LIKE CONCAT('%', #{searchText}, '%') " +
            "</if>" +
            "<if test='filterCategory != null'> " +
            "AND tag_category_id = #{filterCategory} " +
            "</if>" +
            "<if test='filterTag != null and !filterTag.isEmpty()'> " +
            "AND id IN (SELECT question_id FROM slothAsk.question_tag WHERE tag_id IN" +
            "<foreach item='tag' collection='filterTag' open='(' separator=',' close=')'>" +
            "#{tag}" +
            "</foreach>) " +
            "</if>" +
            "<if test='filterType != null'> " +
            "AND type = #{filterType} " +
            "</if>" +
            "<if test='filterDifficulty != null'> " +
            "AND difficulty = #{filterDifficulty} " +
            "</if>" +
            "AND project_id = #{projectId} " +
            "</where>" +
            "LIMIT #{pageNum}, 20" +
            "</script>")
    List<Question> getQuestionListByFilter(
            @Param("projectId") Long projectId,

            @Param("searchText") String searchText,
            @Param("filterCategory") Integer filterCategory,
            @Param("filterTag") List<Integer> filterTag,
            @Param("filterType") Integer filterType,
            @Param("filterDifficulty") Integer filterDifficulty,
            @Param("pageNum") Integer pageNum
    );
}
