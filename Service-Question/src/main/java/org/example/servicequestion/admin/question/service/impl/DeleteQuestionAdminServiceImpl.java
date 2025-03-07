/**
 * File Name: DeleteQuestionAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service.impl;

import org.example.servicequestion.admin.question.service.DeleteQuestionAdminService;
import org.example.servicequestion.admin.question.mapper.AdminQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteQuestionAdminServiceImpl implements DeleteQuestionAdminService {

    @Autowired
    private AdminQuestionMapper adminQuestionMapper;

    @Override
    public void deleteQuestion(Long id) {
        adminQuestionMapper.deleteById(id);
    }

    @Override
    public void deleteQuestions(List<Long> ids) {
        adminQuestionMapper.deleteBatchIds(ids);
    }
}
