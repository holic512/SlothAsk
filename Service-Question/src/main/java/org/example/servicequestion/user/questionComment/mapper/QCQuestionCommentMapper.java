/**
 * File Name: QCQuestionCommentMapper.java
 * Description: 问题评论数据访问层，提供对问题评论数据的CRUD操作
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 * Usage:
 * - 继承MyBatis-Plus的BaseMapper，提供对question_comment表的基本操作
 * - 支持通过MyBatis-Plus内置方法实现评论的增删改查
 * - 主要用于问题评论模块的数据持久化操作
 */
package org.example.servicequestion.user.questionComment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.servicequestion.entity.QuestionComment;

@Mapper
public interface QCQuestionCommentMapper extends BaseMapper<QuestionComment> {
}
