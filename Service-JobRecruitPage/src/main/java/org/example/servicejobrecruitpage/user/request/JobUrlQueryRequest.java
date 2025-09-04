/**
 * File Name: JobUrlQueryRequest.java
 * Description: 岗位申请URL查询请求DTO
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于通过岗位ID查询岗位的申请URL
 */
package org.example.servicejobrecruitpage.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class JobUrlQueryRequest {
    
    /**
     * 岗位ID
     */
    @NotNull(message = "岗位ID不能为空")
    @Positive(message = "岗位ID必须为正数")
    private Long jobId;
}