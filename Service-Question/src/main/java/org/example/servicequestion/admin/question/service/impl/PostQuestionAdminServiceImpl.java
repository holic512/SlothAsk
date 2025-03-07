/**
 * File Name: PostQuestionAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-22
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service.impl;

import org.example.servicequestion.admin.question.dto.uploadImageDto;
import org.example.servicequestion.admin.question.enmus.PostQuestionAdminEnum;
import org.example.servicequestion.admin.question.mapper.AdminQuestionMapper;
import org.example.servicequestion.admin.question.request.AddQuestionRequest;
import org.example.servicequestion.admin.question.service.PostQuestionAdminService;
import org.example.servicequestion.config.Feign.ServiceImageFeign;
import org.example.servicequestion.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class PostQuestionAdminServiceImpl implements PostQuestionAdminService {

    private final ServiceImageFeign serviceImageFeign;
    private final AdminQuestionMapper adminQuestionMapper;

    @Autowired
    public PostQuestionAdminServiceImpl(ServiceImageFeign serviceImageFeign, AdminQuestionMapper adminQuestionMapper) {
        this.serviceImageFeign = serviceImageFeign;
        this.adminQuestionMapper = adminQuestionMapper;
    }

    @Override
    public uploadImageDto uploadImage(MultipartFile file) {

        // 执行图片保存服务
        Map<String, String> result = serviceImageFeign.uploadImage(file);

        // 生成结果类
        uploadImageDto uploadImageDto = new uploadImageDto();
        uploadImageDto.setFileName(result.get("fileName"));
        uploadImageDto.setPreviewUrl(result.get("previewUrl"));

        return uploadImageDto;
    }

    @Override
    public PostQuestionAdminEnum addQuestion(AddQuestionRequest addQuestionRequest) {

        // 构建实体类对象
        Question question = new Question();

        question.setCategoryId(addQuestionRequest.getCategoryId());
        question.setTitle(addQuestionRequest.getTitle());
        question.setContent(addQuestionRequest.getContent());
        question.setAnswer(addQuestionRequest.getAnswer());
        question.setDifficulty(addQuestionRequest.getDifficulty());
        question.setType(addQuestionRequest.getType());
        question.setTagCategoryIds(addQuestionRequest.getTagCategoryId());
        question.setStatus(addQuestionRequest.getStatus());
        question.setProjectId(addQuestionRequest.getProjectId());
        int result = adminQuestionMapper.insert(question);
        if (result == 1) {
            return PostQuestionAdminEnum.SUCCESS;
        } else {
            return PostQuestionAdminEnum.Fail;
        }
    }
}
