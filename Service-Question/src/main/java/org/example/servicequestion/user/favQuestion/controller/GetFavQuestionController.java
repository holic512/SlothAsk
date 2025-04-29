/**
 * File Name: GetFavQuestionController.java
 * Description: 获取用户收藏题目列表控制器
 * Author: holic512
 * Created Date: 2025-04-29
 * Version: 1.0
 * Usage:
 * 提供用户获取收藏列表API接口
 */
package org.example.servicequestion.user.favQuestion.controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.favQuestion.dto.FavQuestionListDTO;
import org.example.servicequestion.user.favQuestion.service.GetFavQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/favQuestion")
public class GetFavQuestionController {
    
    private final GetFavQuestionService getFavQuestionService;
    
    @Autowired
    public GetFavQuestionController(GetFavQuestionService getFavQuestionService) {
        this.getFavQuestionService = getFavQuestionService;
    }
    
    /**
     * 获取用户收藏题目列表
     *
     * @param userId   用户ID，从请求头中获取
     * @param page     页码，默认为1
     * @param pageSize 每页数量，默认为10
     * @param title    题目标题搜索关键词，可选
     * @return 用户收藏题目列表
     */
    @GetMapping("list")
    public ApiResponse getFavList(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title) {
        
        FavQuestionListDTO result = getFavQuestionService.getUserFavQuestions(userId, page, pageSize, title);
        return ApiResponse.ok(result);
    }
    
    /**
     * 检查用户是否收藏了特定题目
     *
     * @param userId    用户ID，从请求头中获取
     * @param virtualId 题目虚拟ID
     * @return 是否已收藏
     */
    @GetMapping("checkFav")
    public ApiResponse checkFavorite(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam String virtualId) {
        
        boolean isFavorited = getFavQuestionService.checkUserFavorite(userId, virtualId);
        return ApiResponse.ok(isFavorited);
    }
}
