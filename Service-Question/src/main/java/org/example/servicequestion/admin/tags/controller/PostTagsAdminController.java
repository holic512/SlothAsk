/**
 * File Name: PostTagsAdminController.java
 * Description: 标签管理系统的新增控制器，提供添加新标签的功能
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   - POST /admin/tags/addtag 添加新标签
 * 
 * 该控制器负责处理与标签添加相关的所有HTTP请求：
 * 1. 新增标签时会进行参数校验
 * 2. 返回统一的ApiResponse响应格式
 * 3. 使用PostTagsAdminEnum枚举表示操作结果
 */
package org.example.servicequestion.admin.tags.controller;

import org.example.servicequestion.admin.tags.enums.PostTagsAdminEnum;
import org.example.servicequestion.admin.tags.request.AddTagsRequest;
import org.example.servicequestion.admin.tags.service.PostTagsAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/tags")
public class PostTagsAdminController {

    private final PostTagsAdminService postTagsAdminService;

    @Autowired
    public PostTagsAdminController(PostTagsAdminService postTagsAdminService) {
        this.postTagsAdminService = postTagsAdminService;
    }

    @PostMapping("/addtag")
    public ApiResponse addTag(@Valid @RequestBody AddTagsRequest addTagsRequest) {

        PostTagsAdminEnum result = postTagsAdminService.AddTag(addTagsRequest);

        if (result == PostTagsAdminEnum.Success) {
            return new ApiResponse(200, "添加成功");
        } else {
            return new ApiResponse(400, "添加失败");
        }

    }
}
