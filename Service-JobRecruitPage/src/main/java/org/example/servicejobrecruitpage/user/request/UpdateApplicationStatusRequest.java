/**
 * File Name: UpdateApplicationStatusRequest.java
 * Description: 更新申请状态请求体
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于接收前端传递的申请状态更新参数
 */
package org.example.servicejobrecruitpage.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {
    
    /**
     * 岗位ID
     */
    @NotNull(message = "岗位ID不能为空")
    private Long jobId;
    
    /**
     * 新的申请状态
     */
    @NotBlank(message = "申请状态不能为空")
    private String applicationStatus;
}