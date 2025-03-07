/**
 * File Name: PostQuestionAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-22
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service;

import org.example.servicequestion.admin.question.dto.uploadImageDto;
import org.example.servicequestion.admin.question.enmus.PostQuestionAdminEnum;
import org.example.servicequestion.admin.question.request.AddQuestionRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PostQuestionAdminService {

    uploadImageDto uploadImage(MultipartFile file);

    PostQuestionAdminEnum addQuestion(AddQuestionRequest addQuestionRequest);
}
