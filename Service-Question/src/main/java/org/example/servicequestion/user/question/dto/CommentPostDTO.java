/**
 * File Name: CommentPostDTO.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-04-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.user.question.dto;

import lombok.Data;

@Data
public class CommentPostDTO {
    String QuestionId;
    Long UserId;
    String Content;
    Long ParentId;
}
