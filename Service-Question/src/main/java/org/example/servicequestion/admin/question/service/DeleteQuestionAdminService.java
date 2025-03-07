/**
 * File Name: DeleteQuestionAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service;

import java.util.List;

public interface DeleteQuestionAdminService {
    void deleteQuestion(Long id);
    void deleteQuestions(List<Long> ids);
}
