/**
 * File Name: GetUserProfileController.java
 * Description: 用户个人资料查询控制器，提供用户信息、评论和收藏问题的查询接口
 * Author: holic512
 * Created Date: 2025-06-12
 * Version: 1.0
 * Usage:
 * 提供REST接口用于获取用户个人资料信息、用户评论列表和用户收藏的问题列表，支持分页查询和用户身份验证
 */
package org.example.serviceuser.user.profile.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.user.profile.dto.CommentDto;
import org.example.serviceuser.user.profile.dto.FavQuestionItemDto;
import org.example.serviceuser.user.profile.dto.UserProfileInfoDto;
import org.example.serviceuser.user.profile.service.GetUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/profile")
public class GetUserProfileController {

    private final GetUserProfileService getUserProfileService;

    @Autowired
    GetUserProfileController(GetUserProfileService getUserProfileService) {
        this.getUserProfileService = getUserProfileService;
    }

    /**
     * 根据用户名查询用户基本信息。
     *
     * <p>该方法接收唯一的用户名作为参数，查询并返回该用户的基础信息，
     * 包括昵称、头像、注册时间、关注数、粉丝数,个人简介，同时判断当前访问是否为本人。</p>
     *
     * @param username      查询的用户名,可能是任何人（唯一标识符）
     * @param currentUserId 通过网关获取的当前用户的id,用于判断是否为本人
     * @return UserProfileDto 用户基本信息传输对象
     */
    @GetMapping("/user/profileInfo/{username}")
    public ApiResponse getUserProfileInfo(@PathVariable String username,
                                          @RequestHeader(value = "X-User-Id") Long currentUserId) {
        UserProfileInfoDto result = getUserProfileService.getUserProfileInfo(username, currentUserId);
        if (result != null) {
            return ApiResponse.ok(result);
        }else
            return ApiResponse.error(404,"当前用户名不存在");
    }

    /**
     * 根据用户名分页查询用户的评论列表。
     *
     * <p>该方法接收用户名作为路径参数，结合分页参数 page 和 size，
     * 查询该用户所发表的评论记录列表。结果按时间倒序排列。</p>
     *
     * @param username 查询的用户名（唯一标识符）
     * @param page     当前页码，从1开始
     * @param size     每页条数，默认10条
     * @return Page<CommentDto> 评论分页数据
     */
    @GetMapping("/user/commentList/{username}")
    public ApiResponse getUserCommentList(@PathVariable String username,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        // 调用 service 层获取分页评论列表
        Page<CommentDto> commentPage = getUserProfileService.getCommentsByUsername(username, page, size);
        return ApiResponse.ok(commentPage);
    }

    /**
 * 根据用户名分页查询用户收藏的问题列表。
 *
 * @param username 查询的用户名（唯一标识符）
 * @param page     当前页码，从1开始
 * @param size     每页条数，默认10条
 * @return Page<FavQuestionItemDto> 收藏问题分页数据
 */
@GetMapping("/user/favQuestionList/{username}")
public ApiResponse getUserFavQuestionList(@PathVariable String username,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
    Page<FavQuestionItemDto> favPage = getUserProfileService.getFavQuestionsByUsername(username, page, size);
    return ApiResponse.ok(favPage);
}



}
