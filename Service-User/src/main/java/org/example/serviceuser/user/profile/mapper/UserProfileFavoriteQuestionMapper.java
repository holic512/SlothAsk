/**
 * File Name: FavQUserFavoriteQuestionMapper.java
 * Description: 用户题目收藏数据库操作接口，提供批量插入、更新、删除与查询收藏题目记录的能力。
 * Author: holic512
 * Created Date: 2025-04-28
 * Version: 1.0
 * Usage:
 * 由 MyBatis 注解实现具体 SQL 操作，支持批量收藏、取消收藏、根据题目标题模糊查询收藏题目等功能。
 */

package org.example.serviceuser.user.profile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.serviceuser.entity.UserFavoriteQuestion;


@Mapper
public interface UserProfileFavoriteQuestionMapper extends BaseMapper<UserFavoriteQuestion> {
   }
