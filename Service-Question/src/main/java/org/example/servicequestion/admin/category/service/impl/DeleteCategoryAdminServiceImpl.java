package org.example.servicequestion.admin.category.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.servicequestion.admin.category.mapper.AdminCategoryMapper;
import org.example.servicequestion.admin.category.service.DeleteCategoryAdminService;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.entity.QuestionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

@Service
@Slf4j
public class DeleteCategoryAdminServiceImpl implements DeleteCategoryAdminService {

    private final AdminCategoryMapper adminCategoryMapper;
    private final ServiceImageFeign serviceImageFeign;

    @Autowired
    public DeleteCategoryAdminServiceImpl(AdminCategoryMapper adminCategoryMapper,
                                        ServiceImageFeign serviceImageFeign) {
        this.adminCategoryMapper = adminCategoryMapper;
        this.serviceImageFeign = serviceImageFeign;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 查询分类信息
        QuestionCategory category = adminCategoryMapper.selectById(id);
        if (category == null) {
            return;
        }

        // 删除数据库记录
        adminCategoryMapper.deleteById(id);

        // 删除成功后，检查是否需要删除图片
        deleteImageIfNeeded(category.getAvatarUrl());
    }

    @Override
    @Transactional
    public void deleteBatchCategories(List<Long> ids) {
        // 查询所有要删除的分类
        List<QuestionCategory> categories = adminCategoryMapper.selectList(
            new LambdaQueryWrapper<QuestionCategory>()
                .in(QuestionCategory::getId, ids)
        );
        
        if (categories.isEmpty()) {
            return;
        }

        // 批量删除数据库记录
        adminCategoryMapper.delete(
            new LambdaQueryWrapper<QuestionCategory>()
                .in(QuestionCategory::getId, ids)
        );

        // 删除成功后，批量删除图片
        for (QuestionCategory category : categories) {
            deleteImageIfNeeded(category.getAvatarUrl());
        }
    }

    /**
     * 删除图片（如果需要）
     * @param avatarUrl 图片URL
     */
    private void deleteImageIfNeeded(String avatarUrl) {
        if (avatarUrl != null && avatarUrl.startsWith("#")) {
                String fileName = avatarUrl.substring(1); // 去掉#号
                serviceImageFeign.deleteImage(fileName);
        }
    }
} 