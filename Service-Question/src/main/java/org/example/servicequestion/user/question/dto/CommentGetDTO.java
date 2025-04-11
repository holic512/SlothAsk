package org.example.servicequestion.user.question.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentGetDTO {
    private Long id;
    private String questionId;
    private Long userId;
    private String content;
    private Long parentId;
    private Integer likeCount;
    private Integer status;
    private LocalDateTime createTime;

    private String nickname; // 来自 user_profile
    private String avatar;

    private List<CommentGetDTO> replies; // 子评论
}
