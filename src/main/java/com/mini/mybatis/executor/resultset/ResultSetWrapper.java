package com.mini.mybatis.executor.resultset;

import com.mini.mybatis.exception.MappingException;
import com.mini.mybatis.exception.TypeException;
import com.mini.mybatis.exception.TypeHandlerException;
import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.type.JdbcType;
import com.mini.mybatis.type.TypeHandler;
import com.mini.mybatis.type.TypeHandlerRegistry;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class ResultSetWrapper implements Iterator<ResultSet> {
    private final ResultSet resultSet;
    private final Configuration configuration;
    private final List<String> columnNames = new ArrayList<>();
    private final List<String> classNames = new ArrayList<>();
    private final List<JdbcType> jdbcTypes = new ArrayList<>();

    public ResultSetWrapper(ResultSet resultSet, Configuration configuration) throws SQLException {
        this.resultSet = resultSet;
        this.configuration = configuration;

        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnNames.add(metaData.getColumnName(i));
            classNames.add(metaData.getColumnClassName(i));
            jdbcTypes.add(JdbcType.valueOf(metaData.getColumnType(i)));
        }
    }

    public TypeHandler<?> getTypeHandler(String columnName) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equalsIgnoreCase(columnName)) {
                return typeHandlerRegistry.resolve(jdbcTypes.get(i));
            }
        }
        throw new TypeException("No handler registered for column: " + columnName);
    }

    public boolean contains(String columnName) {
        return columnNames.contains(columnName);
    }

    public Object getColumnValue(String columnName) {
        if (!columnNames.contains(columnName)) {
            return null;
        }

        TypeHandler<?> typeHandler = this.getTypeHandler(columnName);
        return typeHandler.getResult(resultSet, columnName);
    }

    @Override
    public boolean hasNext() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet next() {
        return resultSet;
    }
}
