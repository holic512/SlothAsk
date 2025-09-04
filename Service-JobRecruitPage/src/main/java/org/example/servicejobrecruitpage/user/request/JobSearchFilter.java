/**
 * File Name: JobSearchFilter.java
 * Description: 岗位搜索过滤条件
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于接收前端传递的岗位搜索过滤条件，包括关键词、申请状态、岗位类型等
 */
package org.example.servicejobrecruitpage.user.request;

import lombok.Data;

import java.util.List;

@Data
public class JobSearchFilter {
    
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