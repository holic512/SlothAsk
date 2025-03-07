/**
 * File Name: GetQuestionAdminServicce.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.servicequestion.admin.question.dto.GetCateAndTagDto;
import org.example.servicequestion.admin.question.dto.GetProjectDto;
import org.example.servicequestion.admin.question.request.QuestionSearchRequest;
import org.example.servicequestion.entity.Question;

import java.util.List;

public interface GetQuestionAdminService {

    // 获取项目列表
    List<GetProjectDto> getProject();

    // 根据项目id 获取其下的 分类与标签
    GetCateAndTagDto getCateAndTagByProjectId(Long projectId);

    IPage<Question> searchQuestion(QuestionSearchRequest params);

    String getContent(Long QuestionId);

    String getAnswer(Long QuestionId);

    GetCateAndTagDto getAllCateAndTag();

}
