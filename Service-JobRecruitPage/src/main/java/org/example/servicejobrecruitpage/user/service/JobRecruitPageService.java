/**
 * File Name: JobRecruitPageService.java
 * Description: 岗位招聘页面业务逻辑接口
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 定义岗位招聘页面相关的业务逻辑接口，包括岗位列表查询和申请状态修改
 */
package org.example.servicejobrecruitpage.user.service;

import org.example.servicejobrecruitpage.user.dto.JobListOptimizedResponse;
import org.example.servicejobrecruitpage.user.dto.JobListResponse;
import org.example.servicejobrecruitpage.user.dto.JobUrlResponse;
import org.example.servicejobrecruitpage.user.request.JobListQueryRequest;
import org.example.servicejobrecruitpage.user.request.JobUrlQueryRequest;
import org.example.servicejobrecruitpage.user.request.UpdateApplicationStatusRequest;

public interface JobRecruitPageService {
    
    /**
     * 查询岗位列表（优化版本，不包含申请URL）
     * @param request 查询请求参数
     * @param userId 用户ID
     * @return 岗位列表响应
     */
    JobListOptimizedResponse getJobListOptimized(JobListQueryRequest request, Long userId);
    
    /**
     * 查询岗位列表（完整版本，包含申请URL）
     * @param request 查询请求参数
     * @param userId 用户ID
     * @return 岗位列表响应
     */
    JobListResponse getJobList(JobListQueryRequest request, Long userId);
    
    /**
     * 根据岗位ID查询申请URL
     * @param request 查询请求参数
     * @return 岗位URL响应
     */
    JobUrlResponse getJobUrl(JobUrlQueryRequest request);
    
    /**
     * 修改申请状态
     * @param request 修改申请状态请求
     * @param userId 用户ID
     * @return 是否修改成功
     */
    Boolean updateApplicationStatus(UpdateApplicationStatusRequest request, Long userId);
}