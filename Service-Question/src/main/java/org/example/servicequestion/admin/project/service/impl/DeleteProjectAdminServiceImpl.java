/**
 * File Name: DeleteProjectAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.servicequestion.admin.project.enums.DeleteProjectAdminEnum;
import org.example.servicequestion.admin.project.enums.DeleteProjectsAdminEnum;
import org.example.servicequestion.admin.project.service.DeleteProjectAdminService;
import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.admin.project.mapper.ProjectCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteProjectAdminServiceImpl extends ServiceImpl<ProjectCategoryMapper,ProjectCategory>
        implements DeleteProjectAdminService {
    /**
     * 删除项目
     * @param id 项目ID
     * @return
     */
    @Override
    public DeleteProjectAdminEnum deleteProject(Long id) {
        try {
            ProjectCategory projectCategory = this.getById(id);
            if (projectCategory == null) {
                return DeleteProjectAdminEnum.PROJECT_NOT_FOUND;
            }

            boolean removed = this.removeById(id);

            if (removed) {
                return DeleteProjectAdminEnum.SUCCESS;
            } else {
                return DeleteProjectAdminEnum.DELETE_FAILED;
            }
        } catch (Exception e) {
            return DeleteProjectAdminEnum.SYSTEM_ERROR;
        }
    }

    /**
     * 批量删除项目
     * @param ids 项目ID列表
     * @return
     */
    @Override
    public DeleteProjectsAdminEnum deleteProjects(List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return DeleteProjectsAdminEnum.NO_IDS_PROVIDED;
            }

            boolean removed = this.removeByIds(ids);

            if (removed) {
                return DeleteProjectsAdminEnum.SUCCESS;
            } else {
                return DeleteProjectsAdminEnum.DELETE_FAILED;
            }
        } catch (Exception e) {
            return DeleteProjectsAdminEnum.SYSTEM_ERROR;
        }
    }
}
