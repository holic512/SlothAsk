/**
 * File Name: PatchQuestionAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.controller;

import org.example.servicequestion.admin.question.service.PatchQuestionAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/question")
public class PatchQuestionAdminController {

    private final PatchQuestionAdminService patchQuestionAdminService;

    @Autowired
    public PatchQuestionAdminController(PatchQuestionAdminService patchQuestionAdminService) {
        this.patchQuestionAdminService = patchQuestionAdminService;
    }

    @PatchMapping("/status")
    public ApiResponse updateQuestionStatus(@RequestParam Long questionId) {
        boolean success = patchQuestionAdminService.updateQuestionStatus(questionId);

        if (success) return new ApiResponse(200, "更新状态成功");
        else return new ApiResponse(400, "更新状态失败");
    }

    @PatchMapping("/question")
    public ApiResponse updateQuestion(@RequestBody Question question) {
        boolean success = patchQuestionAdminService.updateQuestion(question);

        if (success) return new ApiResponse(200, "更新状态成功");
        else return new ApiResponse(400, "更新状态失败");
    }

}
