/**
 * File Name: PostCategoryAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-06
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.category.service.impl;

import org.example.servicequestion.admin.category.service.PostCategoryAdminService;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.entity.QuestionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.example.servicequestion.admin.category.dto.CategoryAddRequest;

import org.example.servicequestion.admin.category.mapper.AdminCategoryMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@Slf4j
public class PostCategoryAdminServiceImpl implements PostCategoryAdminService {


    private final AdminCategoryMapper adminCategoryMapper;
    private final ServiceImageFeign serviceImageFeign;

    @Autowired
    public PostCategoryAdminServiceImpl(AdminCategoryMapper adminCategoryMapper,
                                        ServiceImageFeign serviceImageFeign) {
        this.adminCategoryMapper = adminCategoryMapper;
        this.serviceImageFeign = serviceImageFeign;
    }

    @Override
    public Long addCategory(CategoryAddRequest request, MultipartFile file) {
        // 创建分类实体
        QuestionCategory category = new QuestionCategory();
        category.setProjectId(request.getProjectId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus());
        category.setSortOrder(request.getSortOrder());

        // 管理员创建 默认为1
        category.setCreatorId(1L);

        // 调用保存头像的服务
        Map<String, String> result = serviceImageFeign.uploadImage(file);
        String fileName = result.get("fileName");
        category.setAvatarUrl("#" + fileName);

        // 插入数据
        adminCategoryMapper.insert(category);

        return category.getId();
    }
}
