package com.exmaple.small.mybatis.executor.resultset;

import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.type.JdbcType;
import com.exmaple.small.mybatis.type.TypeHandler;
import com.exmaple.small.mybatis.type.TypeHandlerRegistry;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultSetWrapper implements Iterator<ResultSet> {
    private final ResultSet resultSet;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final List<String> columnNames = new ArrayList<>();
    private final List<String> classNames = new ArrayList<>();
    private final List<JdbcType> jdbcTypes = new ArrayList<>();

    public ResultSetWrapper(ResultSet resultSet, Configuration configuration) throws SQLException {
        this.resultSet = resultSet;
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
            classNames.add(metaData.getColumnClassName(i));
            jdbcTypes.add(JdbcType.forCode(metaData.getColumnType(i)));
        }
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getClassNames() {
        return classNames;
    }

    public JdbcType getJdbcType(String columnName) {
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equalsIgnoreCase(columnName)) {
                return jdbcTypes.get(i);
            }
        }
        return null;
    }

    public TypeHandler<?> getTypeHandler(String columnName) {
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equalsIgnoreCase(columnName)) {
                return typeHandlerRegistry.resolve(jdbcTypes.get(i));
            }
        }
        return null;
    }

    public Object getColumnValue(String columnName) throws SQLException {
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
