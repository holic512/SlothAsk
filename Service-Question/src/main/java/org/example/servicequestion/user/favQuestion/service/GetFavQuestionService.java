/**
 * File Name: GetFavQuestionService.java
 * Description: 获取用户收藏题目列表服务接口
 * Author: holic512
 * Created Date: 2025-04-29
 * Version: 1.0
 * Usage:
 * 提供用户获取收藏列表功能
 */
package org.example.servicequestion.user.favQuestion.service;

import org.example.servicequestion.user.favQuestion.dto.FavQuestionListDTO;

public interface GetFavQuestionService {

    /**
     * 获取用户收藏的题目列表，支持分页和标题搜索
     *
     * @param userId   用户ID
     * @param page     页码（从1开始）
     * @param pageSize 每页数量
     * @param title    题目标题（模糊搜索，可为null）
     * @return 收藏题目列表DTO，包含题目信息和分页信息
     */
    FavQuestionListDTO getUserFavQuestions(Long userId, int page, int pageSize, String title);
    
    /**
     * 检查用户是否收藏了特定题目
     *
     * @param userId    用户ID
     * @param virtualId 题目虚拟ID
     * @return 是否已收藏
     */
    boolean checkUserFavorite(Long userId, String virtualId);
} 