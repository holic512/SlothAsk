/**
 * File Name: UserUserQuestionQuestionMapper.java
 * Description: 用户模块的问题数据访问接口，提供问题基本操作功能
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 * Usage:
 * 继承MyBatis-Plus的BaseMapper，提供对Question实体的基本CRUD操作，用于用户模块中查询问题信息
 */
package org.example.servicequestion.user.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.servicequestion.entity.Question;

/**
 * 用户题目Mapper接口
 */
@Mapper
public interface UserUserQuestionQuestionMapper extends BaseMapper<Question> {

}
