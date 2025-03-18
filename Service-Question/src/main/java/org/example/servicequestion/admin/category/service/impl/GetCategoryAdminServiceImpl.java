package org.example.servicequestion.admin.category.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.servicequestion.admin.category.dto.CategoryDTO;
import org.example.servicequestion.admin.category.dto.CategoryListRequest;
import org.example.servicequestion.admin.category.dto.ProjectOptionDTO;
import org.example.servicequestion.admin.category.mapper.AdminCategoryMapper;
import org.example.servicequestion.admin.category.service.GetCategoryAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.entity.QuestionCategory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetCategoryAdminServiceImpl implements GetCategoryAdminService {


    private final AdminCategoryMapper adminCategoryMapper;
    private final ServiceImageFeign serviceImageFeign;

    @Autowired
    GetCategoryAdminServiceImpl(AdminCategoryMapper adminCategoryMapper, ServiceImageFeign serviceImageFeign) {
        this.adminCategoryMapper = adminCategoryMapper;
        this.serviceImageFeign = serviceImageFeign;
    }


    @Override
    public ApiResponse getCategoryList(CategoryListRequest request) {
        // 计算分页偏移量
        int offset = (request.getPageNum() - 1) * request.getPageSize();

        // 查询列表
        List<QuestionCategory> categoryList;
        Long total;

        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            // 按关键词搜索
            categoryList = adminCategoryMapper.selectListByKeyword(
                    request.getKeyword(), offset, request.getPageSize());
            total = adminCategoryMapper.selectCountByKeyword(request.getKeyword());
        } else if (request.getProjectId() != null) {
            // 按项目ID查询
            categoryList = adminCategoryMapper.selectListByProjectId(
                    request.getProjectId(), offset, request.getPageSize());
            total = adminCategoryMapper.selectCountByProjectId(request.getProjectId());
        } else if (request.getStatus() != null) {
            // 按状态查询
            categoryList = adminCategoryMapper.selectListByStatus(
                    request.getStatus(), offset, request.getPageSize());
            total = adminCategoryMapper.selectCountByStatus(request.getStatus());
        } else {
            // 查询所有
            categoryList = adminCategoryMapper.selectAllList(offset, request.getPageSize());
            total = adminCategoryMapper.selectTotalCount();
        }

        // 转换为DTO
        List<CategoryDTO> dtoList = categoryList.stream()
                .map(category -> {
                    // 将 avatarUrl 替换为真实URL
                    CategoryDTO dto = this.convertToDTO(category);
                    if (dto.getAvatarUrl() != null && dto.getAvatarUrl().startsWith("#")) {
                        // 获取真实的 URL
                        String realUrl = serviceImageFeign.getPreviewUrl(dto.getAvatarUrl().substring(1));
                        // 更新 avatarUrl 为真实URL
                        dto.setAvatarUrl(realUrl);
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建返回数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", dtoList);
        resultMap.put("total", total);

        // 返回结果
        return new ApiResponse(200, "获取分类列表成功", resultMap);
    }

    /**
     * 将实体转换为DTO
     */
    private CategoryDTO convertToDTO(QuestionCategory category) {
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }

    @Override
    public ApiResponse getProjectOptions() {
        try {
            // 获取项目选项列表
            List<ProjectOptionDTO> options = adminCategoryMapper.selectProjectOptions();

            // 构建返回数据
            return new ApiResponse(200, "获取项目选项成功", options);
        } catch (Exception e) {
            return new ApiResponse(500, "获取项目选项失败");
        }
    }
}