/**
 * File Name: QuestionMapper.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-19
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceai.userAnswer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.servicecommon.entity.Question;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
