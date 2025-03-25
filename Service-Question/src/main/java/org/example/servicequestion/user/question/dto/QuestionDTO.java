/**
 * File Name: QuestionDTO.java
 * Description: 题目数据传输对象
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 */
package org.example.servicequestion.user.question.dto;

import lombok.Data;
import org.example.servicequestion.entity.Question;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private Long projectId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String content;
    private String answer;
    private Integer difficulty;
    private Integer type;
    private List<Integer> tagCategoryIds;
    private List<TagDTO> tags;
    private Integer status;
    private Long viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String virtualId;
    
    // 从Question实体转换为DTO
    public static QuestionDTO fromEntity(Question question) {
        if (question == null) {
            return null;
        }
        
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setProjectId(question.getProjectId());
        dto.setCategoryId(question.getCategoryId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setAnswer(question.getAnswer());
        dto.setDifficulty(question.getDifficulty());
        dto.setType(question.getType());
        dto.setTagCategoryIds(question.getTagCategoryIds());
        dto.setStatus(question.getStatus());
        dto.setViewCount(question.getViewCount());
        dto.setCreateTime(question.getCreateTime());
        dto.setUpdateTime(question.getUpdateTime());
        dto.setVirtualId(question.getVirtualId());

        return dto;
    }
} 