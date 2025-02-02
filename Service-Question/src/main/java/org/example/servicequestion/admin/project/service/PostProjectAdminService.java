/**
 * File Name: PostProjectAdminService.java
 * Description: 项目管理服务接口
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 */
package org.example.servicequestion.admin.project.service;

import org.example.servicequestion.admin.project.enums.PostProjectAdminEnum;
import org.example.servicequestion.admin.project.request.AddProjectAdminRequest;

public interface PostProjectAdminService {
    /**
     * 添加新项目
     * @param request 添加项目请求对象，包含项目名称、描述、排序和状态信息
     * @return PostProjectAdminEnum 返回操作结果枚举
     */
    PostProjectAdminEnum addProject(AddProjectAdminRequest request);
}
