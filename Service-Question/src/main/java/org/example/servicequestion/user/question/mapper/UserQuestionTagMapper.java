/**
 * File Name: UserQuestionTagMapper.java
 * Description: 用户问题标签Mapper接口
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.servicequestion.entity.QuestionTagCategory;
import org.example.servicequestion.user.question.dto.TagDTO;

import java.util.List;

@Mapper
public interface UserQuestionTagMapper extends BaseMapper<QuestionTagCategory> {
    
    /**
     * 根据标签ID列表查询标签信息
     * 
     * @param tagIds 标签ID列表
     * @return 标签信息列表
     */
    @Select("<script>SELECT id, name FROM question_tag_category WHERE id IN " +
            "<foreach collection='tagIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach></script>")
    List<TagDTO> selectBatchByIds(List<Long> tagIds);
} 