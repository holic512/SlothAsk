/**
 * File Name: DeleteQuestionAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.controller;

import org.example.servicequestion.admin.question.service.DeleteQuestionAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/question")
public class DeleteQuestionAdminController {


    private final DeleteQuestionAdminService deleteQuestionAdminService;

    @Autowired
    public DeleteQuestionAdminController(DeleteQuestionAdminService deleteQuestionAdminService) {
        this.deleteQuestionAdminService = deleteQuestionAdminService;
    }

    /**
     * 删除单个问题
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteQuestion(@PathVariable Long id) {
        deleteQuestionAdminService.deleteQuestion(id);
    }

    /**
     * 批量删除问题
     */
    @DeleteMapping("/batch")
    @ResponseBody
    public void deleteQuestions(@RequestBody List<Long> ids) {
        deleteQuestionAdminService.deleteQuestions(ids);
    }
}
