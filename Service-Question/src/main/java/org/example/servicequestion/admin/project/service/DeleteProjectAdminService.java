/**
 * File Name: DeleteProjectAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.service;

import org.example.servicequestion.admin.project.enums.DeleteProjectAdminEnum;
import org.example.servicequestion.admin.project.enums.DeleteProjectsAdminEnum;

import java.util.List;

public interface DeleteProjectAdminService {
    /**
     * 删除项目
     * @param id 项目ID
     * @return 删除结果的枚举
     */
    DeleteProjectAdminEnum deleteProject(Long id);

    /**
     * 批量删除项目
     * @param ids 项目ID列表
     * @return 删除结果的枚举
     */
    DeleteProjectsAdminEnum deleteProjects(List<Long> ids);
}
