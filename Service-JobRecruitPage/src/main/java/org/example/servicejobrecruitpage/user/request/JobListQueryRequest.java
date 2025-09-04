/**
 * File Name: JobListQueryRequest.java
 * Description: 岗位列表查询请求体
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于接收前端传递的岗位列表查询参数，包括分页、排序、筛选等条件
 */
package org.example.servicejobrecruitpage.user.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class JobListQueryRequest {
    
    /**
     * 当前页码，从1开始
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer page;
    
    /**
     * 每页显示数量，默认10条
     */
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量必须大于0")
    private Integer pageSize;
    
    /**
     * 排序字段，默认为最晚发布
     * 可选值：最晚发布、最早发布、最晚结束、最早结束
     */
    private String sortBy;
    
    /**
     * 关键词搜索，支持岗位名称、公司名称、岗位描述、工作地点的模糊匹配
     */
    private String keyword;
    
    /**
     * 申请状态筛选，支持多个状态选择
     */
    private List<String> applicationStatuses;
    
    /**
     * 岗位类型筛选，支持多个类型选择
     */
    private List<String> jobTypes;
}