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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.servicequestion.admin.project.enums.GetProjectAdminEnum;
import org.example.servicequestion.admin.project.request.GetProjectAdminRequest;
import org.example.servicequestion.admin.project.service.GetProjectAdminService;
import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.mapper.ProjectCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class GetProjectAdminServiceImpl extends ServiceImpl<ProjectCategoryMapper,ProjectCategory>
        implements GetProjectAdminService{

    /**
     * 获取项目列表
     * @param request 查询请求对象，包含排序和搜索信息
     * @return 项目列表
     */
    @Override
    public GetProjectAdminEnum getProjectList(GetProjectAdminRequest request, List<ProjectCategory> projectList) {
        try {
            QueryWrapper<ProjectCategory> queryWrapper = new QueryWrapper<>();
            String search = request.getSearch();
            if (search != null && !search.isEmpty()) {
                queryWrapper.like("name", search)
                        .or().like("description", search);
            }
            Integer sortType = request.getSortType();
            if (sortType != null) {
                if (sortType == 1) {
                    queryWrapper.orderByAsc("sort_order");
                } else if (sortType == 2) {
                    queryWrapper.orderByDesc("sort_order");
                }
            }
            List<ProjectCategory> result = this.list(queryWrapper);
            projectList.addAll(result);
            return result.isEmpty() ? GetProjectAdminEnum.SEARCH_FAILED : GetProjectAdminEnum.SUCCESS;
        } catch (Exception e) {
            log.error("获取项目列表失败: ", e);
            return GetProjectAdminEnum.SYSTEM_ERROR;
        }
    }
}
