package com.exmaple.small.mybatis.executor.resultset;

import cn.hutool.core.util.ReflectUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.type.TypeHandler;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler {

    private final MappedStatement mappedStatement;

    public DefaultResultSetHandler(MappedStatement mappedStatement) {
        this.mappedStatement = mappedStatement;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> handleResultSet(Statement stmt) throws SQLException {
        try (ResultSet resultSet = stmt.getResultSet()) {
            return (List<E>) resultSetToObj(mappedStatement.getResultType(),
                    new ResultSetWrapper(resultSet, mappedStatement.getConfiguration())
            );
        }
    }

    private List<Object> resultSetToObj(String resultType, ResultSetWrapper rsWrapper) throws SQLException {
        List<Object> resultList = new ArrayList<>();
        try {
            Object result = Class.forName(resultType).getConstructor().newInstance();
            while (rsWrapper.hasNext()) {
                for (String columnName : rsWrapper.getColumnNames()) {
                    Object columnValue = rsWrapper.getColumnValue(columnName);
                    ReflectUtil.setFieldValue(result, columnName, columnValue);
                }
                resultList.add(result);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }


        return resultList;
    }
}
