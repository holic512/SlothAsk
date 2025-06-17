package org.example.servicequestion.user.answer.request; /**
 * File Name: SaveAnswerRequest.java
 * Description: 用户提交答案请求体
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 */


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class SaveAnswerRequest {

    /**
     * 问题的虚拟 ID（如前端使用 UUID 或 hash ID 标识问题）
     */
    @NotBlank(message = "问题ID不能为空")
    private String virtualId;

    /**
     * 用户的回答内容
     */
    @NotBlank(message = "回答内容不能为空")
    private String answer;
}
