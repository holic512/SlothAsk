/**
 * File Name: PatchQuestionAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service.impl;

import org.example.servicequestion.admin.question.mapper.AdminQuestionMapper;
import org.example.servicequestion.admin.question.service.PatchQuestionAdminService;
import org.example.servicequestion.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatchQuestionAdminServiceImpl implements PatchQuestionAdminService {

    private final AdminQuestionMapper adminQuestionMapper;

    @Autowired
    public PatchQuestionAdminServiceImpl(AdminQuestionMapper adminQuestionMapper) {
        this.adminQuestionMapper = adminQuestionMapper;
    }

    @Override
    public boolean updateQuestionStatus(Long questionId) {
        Question question = adminQuestionMapper.selectById(questionId);
        if (question == null) {
            return false;
        }
        // 切换状态（假设 0:禁用, 1:启用）
        question.setStatus(question.getStatus() == 1 ? 0 : 1);
        adminQuestionMapper.updateById(question);
        return true;
    }

    @Override
    public boolean updateQuestion(Question question) {
        int result= adminQuestionMapper.updateById(question);
        return result == 1;
    }
}

