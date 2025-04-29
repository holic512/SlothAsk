/**
 * File Name: PostFavQuestionController.java
 * Description: 收藏题目功能的接口控制器，提供添加与取消收藏的接口。
 * Author: holic512
 * Created Date: 2025-04-23
 * Version: 1.0
 * Usage:
 * 用户通过 POST 请求调用 /user/favQuestion/addFav 添加收藏，通过 /user/favQuestion/removeFav 取消收藏，
 * 请求需携带请求头 X-User-Id 和参数 virtualId（加密后的题目ID）。
 * 控制器将请求转发至 PostFavQuestionService 并返回统一响应结构。
 */

package org.example.servicequestion.user.favQuestion.controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.favQuestion.enums.PostFavQuestionEnum;
import org.example.servicequestion.user.favQuestion.service.PostFavQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/favQuestion")
public class PostFavQuestionController {

    private final PostFavQuestionService postFavQuestionService;

    @Autowired
    public PostFavQuestionController(PostFavQuestionService postFavQuestionService) {
        this.postFavQuestionService = postFavQuestionService;
    }

    @PostMapping("addFav")
    public ApiResponse addFav(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam String virtualId) {
        PostFavQuestionEnum result = postFavQuestionService.addFavQuestion(virtualId, userId);
        if (result == PostFavQuestionEnum.SUCCESS) {
            return ApiResponse.ok();
        } else return ApiResponse.error(400);
    }

    @PostMapping("removeFav")
    public ApiResponse removeFav(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam String virtualId) {
        PostFavQuestionEnum result = postFavQuestionService.removeFavQuestion(virtualId, userId);
        if (result == PostFavQuestionEnum.SUCCESS) {
            return ApiResponse.ok();
        } else return ApiResponse.error(400);
    }


}
