package com.mini.mybatis.type.handler;

import com.mini.mybatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class TimeOnlyTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(index, new java.sql.Time(parameter.getTime()));
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Time sqlTime = rs.getTime(columnIndex);
        return sqlTime == null ? null : new Date(sqlTime.getTime());
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        java.sql.Time sqlTime = rs.getTime(columnName);
        return sqlTime == null ? null : new Date(sqlTime.getTime());
    }
}
