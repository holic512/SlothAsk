/**
 * File Name: ListTypeHandler.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.config.MyBatisPlus;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.*;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class IntegerListTypeHandler extends JacksonTypeHandler {
    public IntegerListTypeHandler() {
        super(new TypeReference<List<Integer>>() {}.getClass());
    }
}

