package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int index, String parameter) throws SQLException {
        ps.setString(index, parameter);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getResult(ResultSet rs, int index) throws SQLException {
        return rs.getString(index);
    }
}
