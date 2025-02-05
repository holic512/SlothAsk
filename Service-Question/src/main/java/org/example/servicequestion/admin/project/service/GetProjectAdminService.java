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
import org.example.servicequestion.admin.project.response.GetProjectAdminResponse;

public interface GetProjectAdminService {

    /**
     * 查询项目列表
     * @param request 查询请求对象，包含排序、搜索和分页信息
     * @return GetProjectAdminResponse 包含总数和项目列表的响应对象
     */
    GetProjectAdminEnum getProjectList(GetProjectAdminRequest request, GetProjectAdminResponse response);
}
