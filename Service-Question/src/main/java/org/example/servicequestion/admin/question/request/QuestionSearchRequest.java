/**
 * File Name: QuestionSearchRequest.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-05
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.request;

import lombok.Data;

@Data
public class QuestionSearchRequest {
    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // 关键字（用于搜索标题和内容）
    private String keyword;

    // 可选条件
    private Long projectId;     // 若业务中有项目字段，可后续扩展使用
    private Long categoryId;
    private Integer difficulty;
    private Integer type;
    private Integer status;
}
