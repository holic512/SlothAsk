/**
 * File Name: PostQuestionAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-22
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.controller;

import org.example.servicequestion.admin.question.dto.uploadImageDto;
import org.example.servicequestion.admin.question.enmus.PostQuestionAdminEnum;
import org.example.servicequestion.admin.question.request.AddQuestionRequest;
import org.example.servicequestion.admin.question.service.PostQuestionAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/question")
public class PostQuestionAdminController {

    private final PostQuestionAdminService postQuestionAdminService;

    @Autowired
    public PostQuestionAdminController(PostQuestionAdminService postQuestionAdminService) {
        this.postQuestionAdminService = postQuestionAdminService;
    }

    // 用于处理图片上传策略
    @PostMapping("uploadImage")
    public ApiResponse uploadImage(@RequestPart("file") MultipartFile file) {

        uploadImageDto result = postQuestionAdminService.uploadImage(file);

        return new ApiResponse(200, "上传图片成功", result);
    }

    // 上传 题目
    @PostMapping("addQuestion")
    public ApiResponse addQuestion(@RequestBody AddQuestionRequest addQuestionRequest) {
        PostQuestionAdminEnum result = postQuestionAdminService.addQuestion(addQuestionRequest);
        if (result == PostQuestionAdminEnum.SUCCESS){
            return new ApiResponse(200,"添加成功");
        }else {
            return new ApiResponse(400,"添加失败");
        }
    }

}
