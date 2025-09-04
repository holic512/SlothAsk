/**
 * File Name: ListTypeHandler.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicejobrecruitpage.config.MyBatisPlus;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeReference;

import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class IntegerListTypeHandler extends JacksonTypeHandler {
    public IntegerListTypeHandler() {
        super(new TypeReference<List<Integer>>() {}.getClass());
    }
}

