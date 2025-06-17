/**
 * File Name: UserProfileQuestionCommentMapper.java
 * Description: 用户个人资料模块的问题评论数据访问接口
 * Author: holic512
 * Created Date: 2025-06-16
 * Version: 1.0
 * Usage:
 * 提供对问题评论实体的基本CRUD操作，用于用户个人资料页面展示用户发表的评论列表
 */
package org.example.serviceuser.user.profile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.serviceuser.entity.QuestionComment;

@Mapper
public interface UserProfileQuestionCommentMapper extends BaseMapper<QuestionComment> {
}
