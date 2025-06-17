/**
 * File Name: FavQuestionItemDto.java
 * Description: 用户收藏问题数据传输对象，用于封装收藏问题的相关信息
 * Author: holic512
 * Created Date: 2025-06-16
 * Version: 1.0
 * Usage:
 * 在用户个人资料页面展示用户收藏的问题列表，包含问题ID、标题、标签、浏览次数和收藏时间
 */
package org.example.serviceuser.user.profile.dto;

import lombok.Data;

import java.util.List;

@Data
public class FavQuestionItemDto {
    /**
     * 问题唯一标识符
     */
    private String questionId;

    /**
     * 问题标题
     */
    private String questionTitle;

    /**
     * 关联标签列表
     */
    private List<String> tags;

    /**
     * 问题的浏览次数
     */
    private Integer views;

    /**
     * 收藏该问题的时间（ISO 字符串格式）
     */
    private String savedAt;
}
