/**
 * File Name: JobRecruitPageController.java
 * Description: 岗位招聘页面控制器
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 提供岗位招聘页面相关的REST API接口，包括岗位列表查询和申请状态修改
 */
package org.example.servicejobrecruitpage.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.servicejobrecruitpage.user.dto.JobListResponse;
import org.example.servicejobrecruitpage.user.enums.JobRecruitPageEnum;
import org.example.servicejobrecruitpage.user.request.JobListQueryRequest;
import org.example.servicejobrecruitpage.user.request.UpdateApplicationStatusRequest;
import org.example.servicejobrecruitpage.user.service.JobRecruitPageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import jakarta.validation.ConstraintViolationException;

@Slf4j
@RestController
@RequestMapping("/job-recruit")
@RequiredArgsConstructor
public class JobRecruitPageController {

    private final JobRecruitPageService jobRecruitPageService;

    /**
     * 查询岗位列表
     *
     * @param request 查询请求参数
     * @param userId  用户ID（从请求头获取）
     * @return 岗位列表响应
     */
    @PostMapping("/list")
    public ApiResponse getJobList(
            @RequestBody @Validated JobListQueryRequest request,
            @RequestHeader(value = "X-User-Id") Long userId) {

        log.info("开始查询岗位列表，用户ID: {}, 查询参数: {}", userId, request);

        try {
            // 参数校验
            if (userId == null || userId <= 0) {
                log.warn("用户ID无效: {}", userId);
                return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                        "用户ID不能为空或无效");
            }

            if (request.getPage() <= 0 || request.getPageSize() <= 0) {
                log.warn("分页参数无效: page={}, pageSize={}", request.getPage(), request.getPageSize());
                return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                        "分页参数必须大于0");
            }

            if (request.getPageSize() > 100) {
                log.warn("分页大小超出限制: {}", request.getPageSize());
                return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                        "每页最多查询100条记录");
            }

            JobListResponse response = jobRecruitPageService.getJobList(request, userId);
            log.info("岗位列表查询成功，用户ID: {}, 返回记录数: {}", userId, response.getJobs().size());
            return ApiResponse.ok(response);

        } catch (ConstraintViolationException e) {
            log.error("参数校验失败，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                    "请求参数格式错误: " + e.getMessage());

        } catch (DataAccessException e) {
            log.error("数据库访问异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.SYSTEM_ERROR.getCode(),
                    "数据库访问异常，请稍后重试");

        } catch (IllegalArgumentException e) {
            log.error("参数异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                    e.getMessage());

        } catch (Exception e) {
            log.error("查询岗位列表异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.QUERY_FAILED.getCode(),
                    "查询失败，请稍后重试");
        }
    }

    /**
     * 修改申请状态
     *
     * @param request 修改申请状态请求
     * @param userId  用户ID（从请求头获取）
     * @return 修改结果
     */
    @PostMapping("/update-status")
    public ApiResponse updateApplicationStatus(
            @RequestBody @Validated UpdateApplicationStatusRequest request,
            @RequestHeader(value = "X-User-Id") Long userId) {

        log.info("开始修改申请状态，用户ID: {}, 请求参数: {}", userId, request);

        try {
            // 参数校验
            if (userId == null || userId <= 0) {
                log.warn("用户ID无效: {}", userId);
                return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                        "用户ID不能为空或无效");
            }

            if (request.getJobId() == null || request.getJobId() <= 0) {
                log.warn("岗位ID无效: {}", request.getJobId());
                return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                        "岗位ID不能为空或无效");
            }

            Boolean result = jobRecruitPageService.updateApplicationStatus(request, userId);
            if (result) {
                log.info("申请状态修改成功，用户ID: {}, 岗位ID: {}", userId, request.getJobId());
                return ApiResponse.ok(result);
            } else {
                log.warn("申请状态修改失败，岗位不存在或无权限，用户ID: {}, 岗位ID: {}", userId, request.getJobId());
                return ApiResponse.error(JobRecruitPageEnum.JOB_NOT_FOUND.getCode(),
                        "岗位不存在或您无权限修改此申请状态");
            }

        } catch (ConstraintViolationException e) {
            log.error("参数校验失败，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                    "请求参数格式错误: " + e.getMessage());

        } catch (DataAccessException e) {
            log.error("数据库访问异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.SYSTEM_ERROR.getCode(),
                    "数据库访问异常，请稍后重试");

        } catch (IllegalArgumentException e) {
            log.error("参数异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.PARAM_ERROR.getCode(),
                    e.getMessage());

        } catch (Exception e) {
            log.error("修改申请状态异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            return ApiResponse.error(JobRecruitPageEnum.UPDATE_FAILED.getCode(),
                    "修改失败，请稍后重试");
        }
    }
}