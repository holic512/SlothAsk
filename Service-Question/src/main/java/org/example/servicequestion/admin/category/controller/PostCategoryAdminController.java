/**
 * File Name: PostCategoryAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-06
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.category.controller;

import jakarta.validation.Valid;
import org.example.servicequestion.admin.category.dto.CategoryAddRequest;
import org.example.servicequestion.admin.category.service.PostCategoryAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.config.Feign.ServiceImageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/admin/category")
@Validated
public class PostCategoryAdminController {

    private static final Logger log = LoggerFactory.getLogger(PostCategoryAdminController.class);

    @Autowired
    private PostCategoryAdminService postCategoryAdminService;


    /**
     * 添加分类
     */
    @PostMapping("/add")
    public ApiResponse addCategory(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") @Valid CategoryAddRequest request

    ) {
        postCategoryAdminService.addCategory(request, file);
        return new ApiResponse(200, "添加成功");
    }

}
