/**
 * File Name: UserProfileController.java
 * Description: 用户个人资料控制器，提供与用户问题相关的信息查询接口
 * Author: holic512
 * Created Date: 2025-06-16
 * Version: 1.0
 * Usage:
 * 提供根据问题ID获取问题信息的REST接口，包括获取问题标题、虚拟ID、标签和访问量等功能
 */
package org.example.servicequestion.user.user.controller;

import org.example.servicequestion.user.commonService.IdConversionService;
import org.example.servicequestion.user.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final IdConversionService idConversionService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, IdConversionService idConversionService) {
        this.userProfileService = userProfileService;
        this.idConversionService = idConversionService;
    }

    @GetMapping("/questionUidAndTitle/{questionId}")
    public Map<String, String> getQuestionInfo(@PathVariable Long questionId) {
        Map<String, String> result = new HashMap<>();
        // 获取虚拟ID
        String virtualId = idConversionService.getVirtualIdFromOriginalId(questionId);
        // 获取问题标题
        String title = userProfileService.getQuestionTitle(questionId);

        result.put("questionTUid", virtualId);
        result.put("questionTitle", title);
        return result;
    }

    /**
     * 获取问题信息
     *
     * @param questionId 问题ID
     * @return 包含问题虚拟ID,标题,标签,访问量的Map
     */
    @GetMapping("/questionUidAndTitleAndTags/{questionId}")
    Map<String, Object> getQuestionUidAndTitleAndTags(@PathVariable("questionId") Long questionId) {
        Map<String, Object> result = new HashMap<>();
        // 获取虚拟ID
        String virtualId = idConversionService.getVirtualIdFromOriginalId(questionId);

        // 获取问题标题
        String title = userProfileService.getQuestionTitle(questionId);

        // 获取问题标签
        List<String> tags = userProfileService.getTagNamesByQuestionId(questionId);

        // 获取问题访问量
        Long viewCount = userProfileService.getQuestionViewCount(questionId);

        result.put("questionTUid", virtualId);
        result.put("questionTitle", title);
        result.put("tags", tags);
        result.put("viewCount", viewCount);
        return result;
    }

}
