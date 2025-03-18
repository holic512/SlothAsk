package org.example.servicequestion.admin.category.service.impl;

import org.example.servicequestion.admin.category.dto.CategoryUpdateRequest;
import org.example.servicequestion.admin.category.mapper.AdminCategoryMapper;
import org.example.servicequestion.admin.category.service.PutCategoryAdminService;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.entity.QuestionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class PutCategoryAdminServiceImpl implements PutCategoryAdminService {

    private final AdminCategoryMapper adminCategoryMapper;
    private final ServiceImageFeign serviceImageFeign;

    @Autowired
    public PutCategoryAdminServiceImpl(AdminCategoryMapper adminCategoryMapper,
                                     ServiceImageFeign serviceImageFeign) {
        this.adminCategoryMapper = adminCategoryMapper;
        this.serviceImageFeign = serviceImageFeign;
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateRequest request, MultipartFile file) {
        // 获取原有分类信息
        QuestionCategory category = adminCategoryMapper.selectById(request.getId());
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        // 如果有新文件上传，先处理图片
        String oldAvatarUrl = category.getAvatarUrl();
        if (file != null) {
            // 上传新图片
            Map<String, String> result = serviceImageFeign.uploadImage(file);
            String fileName = result.get("fileName");
            category.setAvatarUrl("#" + fileName);

            // 如果原来的图片是以#开头，说明是上传的图片，需要删除
            if (oldAvatarUrl != null && oldAvatarUrl.startsWith("#")) {
                try {
                    serviceImageFeign.deleteImage(oldAvatarUrl.substring(1));
                } catch (Exception e) {
                    // 删除旧图片失败，记录日志但不影响主流程
                    e.printStackTrace();
                }
            }
        }

        // 更新分类信息
        category.setProjectId(request.getProjectId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder());
        category.setStatus(request.getStatus());

        // 保存到数据库
        adminCategoryMapper.updateById(category);
    }
} 