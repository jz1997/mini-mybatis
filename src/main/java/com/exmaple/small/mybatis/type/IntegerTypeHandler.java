package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler implements TypeHandler<Integer> {
    @Override
    public void setParameter(PreparedStatement ps, int index, Integer parameter) throws SQLException {
        ps.setInt(index, parameter);
    }

    @Override
    public Integer getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getInt(columnName);
    }

    @Override
    public Integer getResult(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }
}
