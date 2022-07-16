package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler implements TypeHandler<Long> {

    @Override
    public void setParameter(PreparedStatement ps, int index, Long parameter) throws SQLException {
        ps.setLong(index, parameter);
    }

    @Override
    public Long getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getLong(columnName);
    }

    @Override
    public Long getResult(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }
}
