/**
 * File Name: SearchProjectAdminRequest.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-02-03
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.request;

import lombok.Data;

@Data
public class GetProjectAdminRequest {
    // 排序类型: 0 - 不排序, 1 - 按照权重升序, 2 - 按照权重降序
    private Integer sortType;
    // 搜索关键词，可以模糊匹配名称和描述
    private String search;
    // 当前页码
    private Integer pageNum = 1;
    // 每页大小
    private Integer pageSize = 10;
}
