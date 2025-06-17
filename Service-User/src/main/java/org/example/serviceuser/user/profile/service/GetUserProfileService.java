/**
 * File Name: GetUserProfileService.java
 * Description: 用户个人资料服务接口，定义用户信息、评论和收藏问题的查询方法
 * Author: holic512
 * Created Date: 2025-06-12
 * Version: 1.0
 * Usage:
 * 提供获取用户基本信息、用户评论列表和用户收藏问题列表的服务接口，支持分页查询和用户身份验证
 */
package org.example.serviceuser.user.profile.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.serviceuser.user.profile.dto.CommentDto;
import org.example.serviceuser.user.profile.dto.FavQuestionItemDto;
import org.example.serviceuser.user.profile.dto.UserProfileInfoDto;

public interface GetUserProfileService {

    /**
     * 根据用户名查询用户基本信息。
     *
     * <p>该方法接收唯一的用户名作为参数，查询并返回该用户的基础信息，
     * 包括昵称、头像、注册时间、关注数、粉丝数,个人简介，同时判断当前访问是否为本人。</p>
     *
     * @param username 查询的用户名,可能是任何人（唯一标识符）
     * @param currentUserId   通过网关获取的当前用户的id,用于判断是否为本人
     * @return UserProfileDto 用户基本信息传输对象
     */
    UserProfileInfoDto getUserProfileInfo(String username,Long currentUserId);


    Page<CommentDto> getCommentsByUsername(String username,int page,int size);

    Page<FavQuestionItemDto> getFavQuestionsByUsername(String username,int page,int size);
}
