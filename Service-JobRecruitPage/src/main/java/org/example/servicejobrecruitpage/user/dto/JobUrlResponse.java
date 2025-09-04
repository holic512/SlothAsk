/**
 * File Name: JobUrlResponse.java
 * Description: 岗位申请URL响应DTO
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于返回岗位的申请URL信息
 */
package org.example.servicejobrecruitpage.user.dto;

import lombok.Data;

@Data
public class JobUrlResponse {
    
    /**
     * 岗位ID
     */
    private Long jobId;
    
    /**
     * 申请链接
     */
    private String applyUrl;
    
    /**
     * 推荐码
     */
    private String referralCode;
}