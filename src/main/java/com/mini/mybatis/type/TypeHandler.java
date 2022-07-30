package com.mini.mybatis.type;


import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface TypeHandler<T> {
    void setParameter(PreparedStatement ps, int index, T parameter, JdbcType jdbcType);

    T getResult(ResultSet rs, int columnIndex);

    T getResult(ResultSet rs, String columnName);
}
