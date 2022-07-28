package com.mini.mybatis.type.handler;

import com.mini.mybatis.exception.ResultMapException;
import com.mini.mybatis.exception.TypeException;
import com.mini.mybatis.type.JdbcType;
import com.mini.mybatis.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> implements TypeHandler<T> {
    @Override
    public void setParameter(PreparedStatement ps, int index, T parameter, JdbcType jdbcType) {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new TypeException("JdbcType 不能为空");
            }
            try {
                ps.setNull(index, jdbcType.typeCode);
            } catch (SQLException e) {
                throw new TypeException("设置 NULL 参数失败, 失败原因: " + e, e);
            }
        } else {
            try {
                setNonNullParameter(ps, index, parameter, jdbcType);
            } catch (SQLException e) {
                throw new TypeException("设置 NULL 参数失败, 失败原因: " + e, e);
            }
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) {
        try {
            return getNullableResult(rs, columnIndex);
        } catch (SQLException e) {
            throw new ResultMapException("获取结果失败, 失败原因: " + e, e);
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) {
        try {
            return getNullableResult(rs, columnName);
        } catch (SQLException e) {
            throw new ResultMapException("获取结果失败, 失败原因: " + e, e);
        }
    }

    public abstract void setNonNullParameter(PreparedStatement ps, int index, T parameter, JdbcType jdbcType) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;
}
