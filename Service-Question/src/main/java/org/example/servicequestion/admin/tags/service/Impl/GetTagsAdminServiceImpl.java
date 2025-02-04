/**
 * File Name: GetTagsAdminServiceImpl.java
 * Description: 标签管理-查询标签服务实现类
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 处理标签查询相关的业务逻辑，包括项目列表查询和标签列表分页查询
 */
package org.example.servicequestion.admin.tags.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.servicequestion.admin.tags.dto.GetProjectListDto;
import org.example.servicequestion.admin.tags.dto.GetTagsListDto;
import org.example.servicequestion.admin.tags.mapper.AdminTagsMapper;
import org.example.servicequestion.admin.tags.service.GetTagsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GetTagsAdminServiceImpl implements GetTagsAdminService {

    private final AdminTagsMapper adminTagsMapper;

    @Autowired
    public GetTagsAdminServiceImpl(AdminTagsMapper adminTagsMapper) {
        this.adminTagsMapper = adminTagsMapper;
    }

    /**
     * 获取活跃项目列表
     * @return List<GetProjectListDto> 项目列表数据
     */
    @Override
    public List<GetProjectListDto> getProjectList() {
        return adminTagsMapper.selectActiveProjectList();
    }

    /**
     * 分页获取标签列表
     * @param keyword 搜索关键字，可选
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @return Map<String, Object> 包含：
     *         - total: 总记录数
     *         - list: 标签列表数据
     */
    @Override
    public Map<String, Object> getTagsList(String keyword, int pageNum, int pageSize) {
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        // 根据是否有关键字选择不同的查询方法
        List<GetTagsListDto> tagsList;
        Long total;
        
        if (StringUtils.hasText(keyword)) {
            tagsList = adminTagsMapper.selectTagsListWithKeyword(keyword, offset, pageSize);
            total = adminTagsMapper.selectTotalCountWithKeyword(keyword);
        } else {
            tagsList = adminTagsMapper.selectTagsList(offset, pageSize);
            total = adminTagsMapper.selectTotalCount();
        }

        // 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", tagsList);
        
        return result;
    }
}
