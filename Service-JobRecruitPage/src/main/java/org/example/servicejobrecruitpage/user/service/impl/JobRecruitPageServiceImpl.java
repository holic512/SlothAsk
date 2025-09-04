/**
 * File Name: JobRecruitPageServiceImpl.java
 * Description: 岗位招聘页面业务逻辑实现类
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 实现岗位招聘页面相关的业务逻辑，包括岗位列表查询和申请状态修改
 */
package org.example.servicejobrecruitpage.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.entity.JobItem;
import org.example.servicecommon.entity.UserJobApplication;
import org.example.servicejobrecruitpage.user.dto.*;
import org.example.servicejobrecruitpage.user.mapper.JobItemMapper;
import org.example.servicejobrecruitpage.user.mapper.UserJobApplicationMapper;
import org.example.servicejobrecruitpage.user.request.JobListQueryRequest;
import org.example.servicejobrecruitpage.user.request.JobSearchFilter;
import org.example.servicejobrecruitpage.user.request.JobUrlQueryRequest;
import org.example.servicejobrecruitpage.user.request.UpdateApplicationStatusRequest;
import org.example.servicejobrecruitpage.user.service.JobRecruitPageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobRecruitPageServiceImpl implements JobRecruitPageService {
    
    private final JobItemMapper jobItemMapper;
    private final UserJobApplicationMapper userJobApplicationMapper;
    
    @Override
    public JobListOptimizedResponse getJobListOptimized(JobListQueryRequest request, Long userId) {
        log.info("Service层开始处理岗位列表查询（优化版本），用户ID: {}, 页码: {}, 页大小: {}", 
            userId, request.getPage(), request.getPageSize());
        
        try {
            // 构建分页对象
            Page<JobItem> page = new Page<>(request.getPage(), request.getPageSize());
            
            // 构建查询条件
            QueryWrapper<JobItem> queryWrapper = new QueryWrapper<>();
        
        // 获取过滤条件
        JobSearchFilter filter = request.getFilter();
        
        // 关键词搜索（岗位名称、公司名称、岗位描述）
        if (filter != null && StringUtils.hasText(filter.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                .like("job_name", filter.getKeyword())
                .or().like("company_name", filter.getKeyword())
                .or().like("job_description", filter.getKeyword())
            );
        }
        
        // 岗位类型筛选
        if (filter != null && filter.getJobTypes() != null && !filter.getJobTypes().isEmpty()) {
            queryWrapper.in("job_type", filter.getJobTypes());
        }
        
        // 排序处理
        String sortBy = request.getSortBy();
        if (sortBy != null) {
            switch (sortBy) {
                case "最晚发布":
                    queryWrapper.orderByDesc("publish_time");
                    break;
                case "最早发布":
                    queryWrapper.orderByAsc("publish_time");
                    break;
                case "最晚结束":
                    queryWrapper.orderByDesc("end_time");
                    break;
                case "最早结束":
                    // 过滤掉已过期的岗位（结束时间小于当前时间）
                    queryWrapper.ge("end_time", LocalDateTime.now());
                    queryWrapper.orderByAsc("end_time");
                    break;
                default:
                    queryWrapper.orderByDesc("publish_time");
            }
        } else {
            queryWrapper.orderByDesc("publish_time");
        }
        
            // 查询岗位列表
            log.debug("执行岗位查询，查询条件: {}", queryWrapper.getTargetSql());
            IPage<JobItem> jobPage = jobItemMapper.selectPage(page, queryWrapper);
            log.info("岗位查询完成，总记录数: {}, 当前页记录数: {}", 
                jobPage.getTotal(), jobPage.getRecords().size());
            
            // 获取岗位ID列表
            List<Long> jobIds = jobPage.getRecords().stream()
                .map(JobItem::getId)
                .collect(Collectors.toList());
        
            // 查询用户对这些岗位的申请状态（仅当userId不为空时）
            Map<Long, String> applicationStatusMap = Collections.emptyMap();
            if (!jobIds.isEmpty() && userId != null) {
                log.debug("查询用户申请状态，用户ID: {}, 岗位数量: {}", userId, jobIds.size());
                QueryWrapper<UserJobApplication> appQueryWrapper = new QueryWrapper<>();
                appQueryWrapper.eq("user_id", userId)
                    .in("job_id", jobIds);
                
                List<UserJobApplication> applications = userJobApplicationMapper.selectList(appQueryWrapper);
                applicationStatusMap = applications.stream()
                    .collect(Collectors.toMap(
                        UserJobApplication::getJobId,
                        UserJobApplication::getApplicationStatus
                    ));
                log.debug("用户申请状态查询完成，申请记录数: {}", applications.size());
            } else if (userId == null) {
                log.debug("用户ID为空，跳过申请状态查询");
            }
        
            // 转换为响应DTO（不包含申请URL）
            final Map<Long, String> finalApplicationStatusMap = applicationStatusMap;
            List<JobItemListResponse> jobResponses = jobPage.getRecords().stream()
                .map(job -> {
                    JobItemListResponse response = new JobItemListResponse();
                    BeanUtils.copyProperties(job, response);
                    // 不复制applyUrl字段
                    
                    // 设置申请状态（当userId为空时，申请状态为null或默认值）
                    if (userId != null) {
                        String status = finalApplicationStatusMap.get(job.getId());
                        response.setApplicationStatus(status != null ? status : "未申请");
                    } else {
                        // 用户未登录时，不显示申请状态
                        response.setApplicationStatus("待登录");
                    }
                    
                    return response;
                })
                .collect(Collectors.toList());
        
            // 根据申请状态筛选（仅当userId不为空且有申请状态筛选条件时）
            if (userId != null && filter != null && filter.getApplicationStatuses() != null && !filter.getApplicationStatuses().isEmpty()) {
                int beforeFilter = jobResponses.size();
                jobResponses = jobResponses.stream()
                    .filter(job -> {
                        String applicationStatus = job.getApplicationStatus();
                        return applicationStatus != null && filter.getApplicationStatuses().contains(applicationStatus);
                    })
                    .collect(Collectors.toList());
                log.debug("申请状态筛选完成，筛选前: {}, 筛选后: {}", beforeFilter, jobResponses.size());
            } else if (userId == null && filter != null && filter.getApplicationStatuses() != null && !filter.getApplicationStatuses().isEmpty()) {
                log.debug("用户ID为空，跳过申请状态筛选");
            }
            
            // 构建响应对象
            JobListOptimizedResponse response = new JobListOptimizedResponse();
            response.setJobs(jobResponses);
            response.setCurrentPage(request.getPage());
            response.setPageSize(request.getPageSize());
            response.setTotal(jobPage.getTotal());
            response.setTotalPages((int) jobPage.getPages());
            response.setHasNext(jobPage.getCurrent() < jobPage.getPages());
            response.setHasPrevious(jobPage.getCurrent() > 1);
            
            log.info("岗位列表查询处理完成（优化版本），用户ID: {}, 最终返回记录数: {}", userId, jobResponses.size());
            return response;
            
        } catch (Exception e) {
            log.error("Service层处理岗位列表查询异常（优化版本），用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            throw new RuntimeException("查询岗位列表失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public JobListResponse getJobList(JobListQueryRequest request, Long userId) {
        log.info("Service层开始处理岗位列表查询，用户ID: {}, 页码: {}, 页大小: {}", 
            userId, request.getPage(), request.getPageSize());
        
        try {
            // 构建分页对象
            Page<JobItem> page = new Page<>(request.getPage(), request.getPageSize());
            
            // 构建查询条件
            QueryWrapper<JobItem> queryWrapper = new QueryWrapper<>();
        
        // 获取过滤条件
        JobSearchFilter filter = request.getFilter();
        
        // 关键词搜索（岗位名称、公司名称、岗位描述）
        if (filter != null && StringUtils.hasText(filter.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                .like("job_name", filter.getKeyword())
                .or().like("company_name", filter.getKeyword())
                .or().like("job_description", filter.getKeyword())
            );
        }
        
        // 岗位类型筛选
        if (filter != null && filter.getJobTypes() != null && !filter.getJobTypes().isEmpty()) {
            queryWrapper.in("job_type", filter.getJobTypes());
        }
        
        // 排序处理
        String sortBy = request.getSortBy();
        if (sortBy != null) {
            switch (sortBy) {
                case "最晚发布":
                    queryWrapper.orderByDesc("publish_time");
                    break;
                case "最早发布":
                    queryWrapper.orderByAsc("publish_time");
                    break;
                case "最晚结束":
                    queryWrapper.orderByDesc("end_time");
                    break;
                case "最早结束":
                    // 过滤掉已过期的岗位（结束时间小于当前时间）
                    queryWrapper.ge("end_time", LocalDateTime.now());
                    queryWrapper.orderByAsc("end_time");
                    break;
                default:
                    queryWrapper.orderByDesc("publish_time");
            }
        } else {
            queryWrapper.orderByDesc("publish_time");
        }
        
            // 查询岗位列表
            log.debug("执行岗位查询，查询条件: {}", queryWrapper.getTargetSql());
            IPage<JobItem> jobPage = jobItemMapper.selectPage(page, queryWrapper);
            log.info("岗位查询完成，总记录数: {}, 当前页记录数: {}", 
                jobPage.getTotal(), jobPage.getRecords().size());
            
            // 获取岗位ID列表
            List<Long> jobIds = jobPage.getRecords().stream()
                .map(JobItem::getId)
                .collect(Collectors.toList());
        
            // 查询用户对这些岗位的申请状态（仅当userId不为空时）
            Map<Long, String> applicationStatusMap = Collections.emptyMap();
            if (!jobIds.isEmpty() && userId != null) {
                log.debug("查询用户申请状态，用户ID: {}, 岗位数量: {}", userId, jobIds.size());
                QueryWrapper<UserJobApplication> appQueryWrapper = new QueryWrapper<>();
                appQueryWrapper.eq("user_id", userId)
                    .in("job_id", jobIds);
                
                List<UserJobApplication> applications = userJobApplicationMapper.selectList(appQueryWrapper);
                applicationStatusMap = applications.stream()
                    .collect(Collectors.toMap(
                        UserJobApplication::getJobId,
                        UserJobApplication::getApplicationStatus
                    ));
                log.debug("用户申请状态查询完成，申请记录数: {}", applications.size());
            } else if (userId == null) {
                log.debug("用户ID为空，跳过申请状态查询");
            }
        
            // 转换为响应DTO
            final Map<Long, String> finalApplicationStatusMap = applicationStatusMap;
            List<JobItemResponse> jobResponses = jobPage.getRecords().stream()
                .map(job -> {
                    JobItemResponse response = new JobItemResponse();
                    BeanUtils.copyProperties(job, response);
                    
                    // 设置申请状态（当userId为空时，申请状态为null或默认值）
                    if (userId != null) {
                        String status = finalApplicationStatusMap.get(job.getId());
                        response.setApplicationStatus(status != null ? status : "未申请");
                    } else {
                        // 用户未登录时，不显示申请状态
                        response.setApplicationStatus("待登录");
                    }
                    
                    return response;
                })
                .collect(Collectors.toList());
        
            // 根据申请状态筛选（仅当userId不为空且有申请状态筛选条件时）
            if (userId != null && filter != null && filter.getApplicationStatuses() != null && !filter.getApplicationStatuses().isEmpty()) {
                int beforeFilter = jobResponses.size();
                jobResponses = jobResponses.stream()
                    .filter(job -> {
                        String applicationStatus = job.getApplicationStatus();
                        return applicationStatus != null && filter.getApplicationStatuses().contains(applicationStatus);
                    })
                    .collect(Collectors.toList());
                log.debug("申请状态筛选完成，筛选前: {}, 筛选后: {}", beforeFilter, jobResponses.size());
            } else if (userId == null && filter != null && filter.getApplicationStatuses() != null && !filter.getApplicationStatuses().isEmpty()) {
                log.debug("用户ID为空，跳过申请状态筛选");
            }
            
            // 构建响应对象
            JobListResponse response = new JobListResponse();
            response.setJobs(jobResponses);
            response.setCurrentPage(request.getPage());
            response.setPageSize(request.getPageSize());
            response.setTotal(jobPage.getTotal());
            response.setTotalPages((int) jobPage.getPages());
            response.setHasNext(jobPage.getCurrent() < jobPage.getPages());
            response.setHasPrevious(jobPage.getCurrent() > 1);
            
            log.info("岗位列表查询处理完成，用户ID: {}, 最终返回记录数: {}", userId, jobResponses.size());
            return response;
            
        } catch (Exception e) {
            log.error("Service层处理岗位列表查询异常，用户ID: {}, 错误信息: {}", userId, e.getMessage(), e);
            throw new RuntimeException("查询岗位列表失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public JobUrlResponse getJobUrl(JobUrlQueryRequest request) {
        log.info("Service层开始处理岗位URL查询，岗位ID: {}", request.getJobId());
        
        try {
            // 查询岗位信息
            JobItem jobItem = jobItemMapper.selectById(request.getJobId());
            if (jobItem == null) {
                log.warn("岗位不存在，岗位ID: {}", request.getJobId());
                throw new IllegalArgumentException("岗位不存在");
            }
            
            // 构建响应对象
            JobUrlResponse response = new JobUrlResponse();
            response.setJobId(jobItem.getId());
            response.setApplyUrl(jobItem.getApplyUrl());
            response.setReferralCode(jobItem.getReferralCode());
            
            log.info("岗位URL查询处理完成，岗位ID: {}", request.getJobId());
            return response;
            
        } catch (Exception e) {
            log.error("Service层处理岗位URL查询异常，岗位ID: {}, 错误信息: {}", request.getJobId(), e.getMessage(), e);
            throw new RuntimeException("查询岗位URL失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional
    public Boolean updateApplicationStatus(UpdateApplicationStatusRequest request, Long userId) {
        // 检查岗位是否存在
        JobItem jobItem = jobItemMapper.selectById(request.getJobId());
        if (jobItem == null) {
            return false;
        }
        
        // 查询用户是否已有申请记录
        QueryWrapper<UserJobApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
            .eq("job_id", request.getJobId());
        
        UserJobApplication existingApplication = userJobApplicationMapper.selectOne(queryWrapper);
        
        if (existingApplication != null) {
            // 更新现有申请状态
            existingApplication.setApplicationStatus(request.getApplicationStatus());
            existingApplication.setUpdatedAt(LocalDateTime.now());
            return userJobApplicationMapper.updateById(existingApplication) > 0;
        } else {
            // 创建新的申请记录
            UserJobApplication newApplication = new UserJobApplication();
            newApplication.setUserId(userId);
            newApplication.setJobId(request.getJobId());
            newApplication.setApplicationStatus(request.getApplicationStatus());
            newApplication.setCreatedAt(LocalDateTime.now());
            newApplication.setUpdatedAt(LocalDateTime.now());
            return userJobApplicationMapper.insert(newApplication) > 0;
        }
    }
}