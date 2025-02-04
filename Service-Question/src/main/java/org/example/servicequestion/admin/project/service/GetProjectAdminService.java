/**
 * File Name: GetProjectAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.service;

import org.example.servicequestion.admin.project.enums.GetProjectAdminEnum;
import org.example.servicequestion.admin.project.request.GetProjectAdminRequest;
import org.example.servicequestion.entity.ProjectCategory;

import java.util.List;

public interface GetProjectAdminService {

    /**
     * 查询项目列表
     * @param request 查询请求对象，包含排序和搜索信息
     * @return 项目列表
     */
    GetProjectAdminEnum getProjectList(GetProjectAdminRequest request, List<ProjectCategory> projectList);
}
