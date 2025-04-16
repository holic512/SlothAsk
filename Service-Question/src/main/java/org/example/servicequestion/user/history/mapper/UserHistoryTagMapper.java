/**
 * File Name: UserQuestionTagMapper.java
 * Description: 用户问题标签Mapper接口
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.QuestionTagCategory;
import org.example.servicequestion.user.history.dto.TagSimpleDTO;
import org.example.servicequestion.user.question.dto.TagDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserHistoryTagMapper extends BaseMapper<QuestionTagCategory> {

    @Select({
            "<script>",
            "SELECT id, name FROM question_tag_category",
            "WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<TagSimpleDTO> selectIdAndNameByIds(@Param("ids") Set<Integer> ids);

}