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

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public class ListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final String DELIM = ",";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        String value = StringUtils.collectionToDelimitedString(strings, DELIM);
        preparedStatement.setString(i, value);
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, DELIM));
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, DELIM));
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, DELIM));
    }
}
