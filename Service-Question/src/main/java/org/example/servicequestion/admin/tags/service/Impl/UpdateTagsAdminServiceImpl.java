/**
 * File Name: UpdateTagsAdminServiceImpl.java
 * Description: 标签管理-更新标签服务实现类
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 处理标签更新相关的业务逻辑
 */
package org.example.servicequestion.admin.tags.service.Impl;

import org.example.servicequestion.admin.tags.dto.UpdateTagDto;
import org.example.servicequestion.admin.tags.mapper.AdminTagsMapper;
import org.example.servicequestion.admin.tags.service.UpdateTagsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UpdateTagsAdminServiceImpl implements UpdateTagsAdminService {

    private final AdminTagsMapper adminTagsMapper;

    @Autowired
    public UpdateTagsAdminServiceImpl(AdminTagsMapper adminTagsMapper) {
        this.adminTagsMapper = adminTagsMapper;
    }

    /**
     * 更新标签信息
     * @param updateTagDto 标签更新数据传输对象，包含：
     *                     - id: 标签ID
     *                     - projectId: 项目ID
     *                     - name: 标签名称
     *                     - description: 标签描述
     *                     - sortOrder: 排序顺序
     *                     - status: 标签状态
     * @return boolean 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    public boolean updateTag(UpdateTagDto updateTagDto) {
        try {
            return adminTagsMapper.updateTag(
                updateTagDto.getId(),
                updateTagDto.getProjectId(),
                updateTagDto.getName(),
                updateTagDto.getDescription(),
                updateTagDto.getSortOrder(),
                updateTagDto.getStatus()
            ) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}