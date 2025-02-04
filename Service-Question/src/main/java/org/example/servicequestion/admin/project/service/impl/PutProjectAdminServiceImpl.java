/**
 * File Name: PutProjectAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.servicequestion.admin.project.enums.PutProjectAdminEnum;
import org.example.servicequestion.admin.project.request.PutProjectAdminRequest;
import org.example.servicequestion.admin.project.service.PutProjectAdminService;
import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.mapper.ProjectCategoryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PutProjectAdminServiceImpl extends ServiceImpl<ProjectCategoryMapper,ProjectCategory>
    implements PutProjectAdminService {

    /**
     * 编辑项目信息
     * @param request
     * @return
     */
    @Override
    public PutProjectAdminEnum modifyProject(PutProjectAdminRequest request) {
        try {
            ProjectCategory projectCategory = this.getById(request.getId());
            if (projectCategory == null) {
                return PutProjectAdminEnum.PROJECT_NOT_FOUND;
            }

            // Update project information
            projectCategory.setName(request.getProjectName());
            projectCategory.setDescription(request.getProjectDescription());
            projectCategory.setSortOrder(request.getSort());
            projectCategory.setStatus(request.getStatus());
            projectCategory.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(projectCategory);

            if (updated) {
                return PutProjectAdminEnum.SUCCESS;
            } else {
                return PutProjectAdminEnum.UPDATE_FAILED;
            }

        } catch (Exception e) {
            return PutProjectAdminEnum.SYSTEM_ERROR;
        }

    }
}
