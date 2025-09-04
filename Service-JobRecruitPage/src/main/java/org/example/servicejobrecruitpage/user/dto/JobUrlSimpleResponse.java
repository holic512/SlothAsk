/**
 * File Name: JobUrlSimpleResponse.java
 * Description: 简化的岗位申请URL响应DTO
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于返回岗位的申请URL信息（仅包含URL）
 */
package org.example.servicejobrecruitpage.user.dto;

import lombok.Data;

@Data
public class JobUrlSimpleResponse {
    
    /**
     * 申请链接
     */
    private String applyUrl;
    
    public JobUrlSimpleResponse() {}
    
    public JobUrlSimpleResponse(String applyUrl) {
        this.applyUrl = applyUrl;
    }
}