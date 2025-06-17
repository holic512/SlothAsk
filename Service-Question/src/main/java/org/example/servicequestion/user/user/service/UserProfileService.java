/**
 * File Name: UserProfileService.java
 * Description: 用户个人资料服务接口，定义与用户问题相关的信息查询方法
 * Author: holic512
 * Created Date: 2025-06-16
 * Version: 1.0
 * Usage:
 * 提供根据问题ID获取问题标题、标签列表和访问量等功能，用于用户个人资料页面的问题信息展示
 */
package org.example.servicequestion.user.user.service;

import java.util.List;

public interface UserProfileService {
    /**
     * 根据问题ID获取问题标题
     * @param questionId 问题ID
     * @return 问题标题
     */
    String getQuestionTitle(Long questionId);

    /**
     * 根据问题ID获取标签名称列表
     * @param questionId 问题ID
     * @return 标签名称列表
     */
    List<String> getTagNamesByQuestionId(Long questionId);

    /**
     * 根据问题ID获取问题访问量
     * @param questionId 问题ID
     * @return 问题访问量
     */
    Long getQuestionViewCount(Long questionId);
}
