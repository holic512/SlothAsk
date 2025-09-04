/**
 * File Name: JobListResponse.java
 * Description: 岗位列表响应DTO，用于返回分页的岗位列表信息
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 封装岗位列表的响应数据，包含分页信息和岗位列表
 */
package org.example.servicejobrecruitpage.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobListResponse {
    
    /**
     * 岗位列表
     */
    private List<JobItemResponse> jobs;
    
    /**
     * 当前页码
     */
    private Integer currentPage;
    
    /**
     * 每页数量
     */
    private Integer pageSize;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
}