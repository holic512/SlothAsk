/**
 * File Name: GetProjectAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.service.impl;

import org.example.servicequestion.admin.project.enums.GetProjectAdminEnum;
import org.example.servicequestion.admin.project.request.GetProjectAdminRequest;
import org.example.servicequestion.admin.project.response.GetProjectAdminResponse;
import org.example.servicequestion.admin.project.service.GetProjectAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.example.servicequestion.admin.project.dto.GetProjectListDto;
import org.example.servicequestion.admin.project.mapper.AdminProjectMapper;

import java.util.List;

@Service
@Slf4j
public class GetProjectAdminServiceImpl implements GetProjectAdminService {

    @Autowired
    private AdminProjectMapper adminProjectMapper;

    /**
     * 获取项目列表
     * @param request 查询请求对象，包含排序和搜索信息
     * @return 项目列表
     */
    @Override
    public GetProjectAdminEnum getProjectList(GetProjectAdminRequest request, GetProjectAdminResponse response) {
        try {
            // 计算偏移量
            int offset = (request.getPageNum() - 1) * request.getPageSize();
            
            // 获取列表数据
            List<GetProjectListDto> projectList;
            Long total;
            
            // 获取排序类型，如果为空则默认为0
            Integer sortType = request.getSortType() != null ? request.getSortType() : 0;
            
            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                // 带关键字搜索
                switch (sortType) {
                    case 1:
                        projectList = adminProjectMapper.selectProjectListWithKeywordSortAsc(
                            request.getSearch(), offset, request.getPageSize());
                        break;
                    case 2:
                        projectList = adminProjectMapper.selectProjectListWithKeywordSortDesc(
                            request.getSearch(), offset, request.getPageSize());
                        break;
                    default:
                        projectList = adminProjectMapper.selectProjectListWithKeywordDefault(
                            request.getSearch(), offset, request.getPageSize());
                }
                total = adminProjectMapper.selectTotalCountWithKeyword(request.getSearch());
            } else {
                // 不带关键字搜索
                switch (sortType) {
                    case 1:
                        projectList = adminProjectMapper.selectProjectListSortAsc(
                            offset, request.getPageSize());
                        break;
                    case 2:
                        projectList = adminProjectMapper.selectProjectListSortDesc(
                            offset, request.getPageSize());
                        break;
                    default:
                        projectList = adminProjectMapper.selectProjectListDefault(
                            offset, request.getPageSize());
                }
                total = adminProjectMapper.selectTotalCount();
            }
            
            // 设置响应数据
            response.setList(projectList);
            response.setTotal(total);
            
            return projectList.isEmpty() ? GetProjectAdminEnum.SEARCH_FAILED : GetProjectAdminEnum.SUCCESS;
        } catch (Exception e) {
            log.error("获取项目列表失败: ", e);
            return GetProjectAdminEnum.SYSTEM_ERROR;
        }
    }
}
