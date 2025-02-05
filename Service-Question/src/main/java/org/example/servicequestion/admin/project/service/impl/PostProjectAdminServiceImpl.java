/**
 * File Name: PostProjectAdminServiceImpl.java
 * Description: 项目管理服务实现类
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 */
package org.example.servicequestion.admin.project.service.impl;

import org.example.servicequestion.admin.project.enums.PostProjectAdminEnum;
import org.example.servicequestion.admin.project.mapper.AdminProjectMapper;
import org.example.servicequestion.admin.project.request.AddProjectAdminRequest;
import org.example.servicequestion.admin.project.service.PostProjectAdminService;
import org.example.servicequestion.entity.ProjectCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostProjectAdminServiceImpl implements PostProjectAdminService {


    private final AdminProjectMapper adminProjectMapper;

    @Autowired
    public PostProjectAdminServiceImpl(AdminProjectMapper adminProjectMapper) {
        this.adminProjectMapper = adminProjectMapper;
    }

    /**
     * 添加新项目的具体实现
     *
     * @param request 添加项目请求对象
     * @return PostProjectAdminEnum 返回操作结果枚举
     */
    @Override
    public PostProjectAdminEnum addProject(AddProjectAdminRequest request) {
        try {
            // 创建新的项目分类实体
            ProjectCategory projectCategory = new ProjectCategory();
            projectCategory.setName(request.getProjectName());
            projectCategory.setDescription(request.getProjectDescription());
            projectCategory.setSortOrder(request.getSort());
            projectCategory.setStatus(request.getStatus());
            projectCategory.setCreateTime(LocalDateTime.now());
            projectCategory.setUpdateTime(LocalDateTime.now());

            // 插入
            int result = adminProjectMapper.insert(projectCategory);

            return (result > 0) ? PostProjectAdminEnum.SUCCESS : PostProjectAdminEnum.SAVE_FAILED;
        } catch (Exception e) {
            return PostProjectAdminEnum.SYSTEM_ERROR;
        }
    }
}
