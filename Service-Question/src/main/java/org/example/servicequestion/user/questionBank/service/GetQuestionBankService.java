package org.example.servicequestion.user.questionBank.service;

import org.example.servicequestion.entity.ProjectCategory;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;

import java.util.List;

public interface GetQuestionBankService {

    // 根据项目id获取题库
    List<QuestionCategory> getCategoriesByProjectId(Long projectId);

    // 获取所有项目
    List<ProjectCategory> getProjects();

    // 根据题库id获取问题数量
    int getCountByCategoryId(Long categoryId);

    // 根据题库id获取题库
    QuestionCategory getCategoryById(Long categoryId);

    // 根据题库id获取问题
    List<Question> getQuestionsByCategoryId(Long categoryId);
}
