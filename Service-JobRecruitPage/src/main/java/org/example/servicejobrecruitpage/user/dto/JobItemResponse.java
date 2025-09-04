/**
 * File Name: JobItemResponse.java
 * Description: 岗位信息响应DTO，用于返回岗位详细信息
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 封装岗位信息的响应数据，包含岗位基本信息、公司信息、申请状态等
 */
package org.example.servicejobrecruitpage.user.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobItemResponse {
    
    /**
     * 岗位唯一标识符
     */
    private Long id;
    
    /**
     * 公司名称
     */
    private String companyName;
    
    /**
     * 岗位名称
     */
    private String jobName;
    
    /**
     * 岗位描述
     */
    private String jobDescription;
    
    /**
     * 岗位类型
     */
    private String jobType;
    
    /**
     * 福利待遇列表
     */
    private List<String> benefits;
    
    /**
     * 工作地点
     */
    private String location;
    
    /**
     * 薪资范围
     */
    private String salaryRange;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 申请截止时间
     */
    private LocalDateTime endTime;
    
    /**
     * 申请链接
     */
    private String applyUrl;
    
    /**
     * 推荐码
     */
    private String referralCode;
    
    /**
     * 申请状态（当前用户对该岗位的申请状态）
     */
    private String applicationStatus;
    
    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
    
    /**
     * 状态最后更新时间
     */
    private LocalDateTime statusUpdateTime;
}