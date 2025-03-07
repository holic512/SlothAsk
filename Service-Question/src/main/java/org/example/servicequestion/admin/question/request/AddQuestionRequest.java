/**
 * File Name: AddQuestionRequest.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-05
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.request;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AddQuestionRequest {

    // 无实际用处
    @JsonProperty("projectId")
    private Long projectId;

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("difficulty")
    private Integer difficulty;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("tagCategoryId")
    private List<Integer> tagCategoryId;

    @JsonProperty("status")
    private Integer status;

    @Override
    public String toString() {
        return "AddQuestionRequest [projectId=" + projectId + ", categoryId=" + categoryId + ", title="
                + title + ", content=" + content + ", answer=" + answer + ", difficulty=" + difficulty
                + ", type=" + type + ", tagCategoryId=" + tagCategoryId + ", status=" + status + "]";
    }
}
