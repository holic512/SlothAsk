/**
 * File Name: PatchQuestionAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service;

import org.example.servicequestion.entity.Question;

public interface PatchQuestionAdminService {

    boolean updateQuestionStatus(Long questionId);

    boolean updateQuestion(Question question);
}
