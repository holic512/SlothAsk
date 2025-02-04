/**
 * File Name: PostTagsAdminServiceImpl.java
 * Description: 标签管理-新增标签服务实现类
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 处理标签新增相关的业务逻辑
 */
package org.example.servicequestion.admin.tags.service.Impl;

import org.example.servicequestion.admin.tags.enums.PostTagsAdminEnum;
import org.example.servicequestion.admin.tags.mapper.AdminTagsMapper;
import org.example.servicequestion.admin.tags.request.AddTagsRequest;
import org.example.servicequestion.admin.tags.service.PostTagsAdminService;
import org.example.servicequestion.entity.QuestionTagCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.example.servicequestion.admin.tags.enums.PostTagsAdminEnum.*;

@Service
public class PostTagsAdminServiceImpl implements PostTagsAdminService {

    private final AdminTagsMapper adminTagsMapper;

    @Autowired
    public PostTagsAdminServiceImpl(AdminTagsMapper adminTagsMapper) {
        this.adminTagsMapper = adminTagsMapper;
    }

    /**
     * 新增标签
     * @param addTagsRequest 新增标签请求对象，包含：
     *                      - name: 标签名称
     *                      - description: 标签描述
     *                      - project_id: 项目ID
     *                      - sort_order: 排序顺序
     *                      - status: 标签状态
     * @return PostTagsAdminEnum 新增结果枚举：
     *         - Success: 新增成功
     *         - FAILURE: 新增失败
     */
    @Override
    public PostTagsAdminEnum AddTag(AddTagsRequest addTagsRequest) {
        QuestionTagCategory questionTagCategory = new QuestionTagCategory();
        questionTagCategory.setName(addTagsRequest.getName());
        questionTagCategory.setDescription(addTagsRequest.getDescription());
        questionTagCategory.setProjectId(addTagsRequest.getProject_id());
        questionTagCategory.setSortOrder(addTagsRequest.getSort_order());
        questionTagCategory.setStatus(addTagsRequest.getStatus());

        int resultCount = adminTagsMapper.insert(questionTagCategory);
        if (resultCount == 1) {
            return Success;
        } else {
            return FAILURE;
        }
    }
}
