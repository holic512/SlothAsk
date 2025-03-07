/**
 * File Name: GetQuestionAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.servicequestion.admin.question.dto.GetCateAndTagDto;
import org.example.servicequestion.admin.question.dto.GetProjectDto;
import org.example.servicequestion.admin.question.request.QuestionSearchRequest;
import org.example.servicequestion.admin.question.service.GetQuestionAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/question")
public class GetQuestionAdminController {

    private final GetQuestionAdminService getQuestionAdminService;

    @Autowired
    public GetQuestionAdminController(GetQuestionAdminService getQuestionAdminService) {
        this.getQuestionAdminService = getQuestionAdminService;
    }

    // 获取项目列表
    @GetMapping("project")
    public ApiResponse getProject() {
        // 调用服务端
        List<GetProjectDto> result = getQuestionAdminService.getProject();
        return new ApiResponse(200, "获取列表成功", result);
    }

    // 获取对应项目id下的 分类名称和标签名称
    @GetMapping("CateAndTag")
    public ApiResponse getCateAndTag(@RequestParam Long ProjectId) {
        GetCateAndTagDto result = getQuestionAdminService.getCateAndTagByProjectId(ProjectId);
        return new ApiResponse(200, "获取列表成功", result);
    }

    // 获取问题列表 没有内容和答案
    @GetMapping("/search")
    public ApiResponse search(QuestionSearchRequest params) {
        IPage<Question> result = getQuestionAdminService.searchQuestion(params);
        return new ApiResponse(200, "获取列表成功", result);
    }

    // 获取问题 内容
    @GetMapping("content")
    public ApiResponse getContent(@RequestParam Long QuestionId) {
        String result = getQuestionAdminService.getContent(QuestionId);
        return new ApiResponse(200, "查询题目内容成功", result);
    }

    // 获取问题 答案
    @GetMapping("answer")
    public ApiResponse getAnswer(@RequestParam Long QuestionId) {
        String result = getQuestionAdminService.getAnswer(QuestionId);
        return new ApiResponse(200, "查询题目答案成功", result);
    }

    // 获取所有分类列表和标签列表
    @GetMapping("AllCateAndTag")
    public ApiResponse getAllCateAndTag() {
        GetCateAndTagDto result = getQuestionAdminService.getAllCateAndTag();
        return new ApiResponse(200, "查询所有分类列表和标签列表成功", result);
    }


}
