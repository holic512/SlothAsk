/**
 * File Name: DeleteTagsAdminServiceImpl.java
 * Description: 标签管理-删除标签服务实现类
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 处理标签删除相关的业务逻辑，支持单个删除和批量删除
 */
package org.example.servicequestion.admin.tags.service.Impl;

import java.util.List;

import org.example.servicequestion.admin.tags.mapper.AdminTagsMapper;
import org.example.servicequestion.admin.tags.service.DeleteTagsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DeleteTagsAdminServiceImpl implements DeleteTagsAdminService {

    private final AdminTagsMapper adminTagsMapper;

    @Autowired
    public DeleteTagsAdminServiceImpl(AdminTagsMapper adminTagsMapper) {
        this.adminTagsMapper = adminTagsMapper;
    }

    /**
     * 删除单个标签
     * @param id 标签ID
     * @return boolean 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    public boolean deleteTag(Long id) {
        try {
            return adminTagsMapper.deleteById(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批量删除标签
     * @param ids 标签ID列表
     * @return boolean 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    public boolean batchDeleteTags(List<Long> ids) {
        try {
            return adminTagsMapper.deleteBatchIds(ids) > 0;
        } catch (Exception e) {
            return false;
        }
    }
} 