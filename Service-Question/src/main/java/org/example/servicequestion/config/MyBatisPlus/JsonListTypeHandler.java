/**
 * File Name: JsonListTypeHandler.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.config.MyBatisPlus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JsonListTypeHandler extends BaseTypeHandler<List<Integer>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 将 List<Integer> 转换成 JSON 字符串
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new SQLException("Error converting List<Integer> to JSON string", e);
        }
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return convertJsonToList(json);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertJsonToList(json);
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertJsonToList(json);
    }

    private List<Integer> convertJsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            // 将 JSON 字符串转换成 List<Integer>
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to List<Integer>", e);
        }
    }
}

