package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectTypeHandler implements TypeHandler<Object> {

    @Override
    public void setParameter(PreparedStatement ps, int index, Object parameter) throws SQLException {
        ps.setObject(index, parameter);
    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName);
    }

    @Override
    public Object getResult(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index);
    }
}
