package com.mini.mybatis.executor.resultset;

import cn.hutool.core.util.ReflectUtil;
import com.mini.mybatis.exception.MappingException;
import com.mini.mybatis.mapping.MappedStatement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler {
    private final MappedStatement ms;

    public DefaultResultSetHandler(MappedStatement ms) {
        this.ms = ms;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> List<E> handleResultSet(Statement stat) {
        try {
            List<Object> resultObjects = new ArrayList<>();
            Class<?> resultObjClass = Class.forName(ms.getResultType());
            Field[] fields = ReflectUtil.getFields(resultObjClass);

            try (ResultSet resultSet = stat.getResultSet()) {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet, ms.getConfiguration());

                while (resultSetWrapper.hasNext()) {
                    Object o = ReflectUtil.newInstance(resultObjClass);
                    for (Field field : fields) {
                        Object value = resultSetWrapper.getColumnValue(field.getName());
                        ReflectUtil.setFieldValue(o, field.getName(), value);
                    }
                    resultObjects.add(o);
                }
            }
            return (List<E>) resultObjects;
        } catch (SQLException e) {
            throw new MappingException("处理 ResultSet 异常, 原因: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new MappingException("获取返回值类型异常, 原因: " + e.getMessage(), e);
        }
    }
}
