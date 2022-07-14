package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler implements TypeHandler<Boolean> {
    @Override
    public void setParameter(PreparedStatement ps, int index, Boolean parameter) throws SQLException {
        ps.setBoolean(index, parameter);
    }

    @Override
    public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getBoolean(columnName);
    }

    @Override
    public Boolean getResult(ResultSet rs, int index) throws SQLException {
        return rs.getBoolean(index);
    }
}
