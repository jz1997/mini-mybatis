package com.exmaple.small.mybatis.executor.parameter;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.parsing.ParameterMapping;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.type.TypeHandler;
import com.exmaple.small.mybatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultParameterHandler implements ParameterHandler {

    private final MappedStatement mappedStatement;
    private final Configuration configuration;
    private final Object parameterObject;

    public DefaultParameterHandler(
            MappedStatement mappedStatement, Configuration configuration, Object parameterObject) {
        Assert.notNull(mappedStatement, "MappedStatement cannot be null");
        Assert.notNull(configuration, "Configuration cannot be null");
        Assert.notNull(parameterObject, "ParameterObject cannot be null");
        this.mappedStatement = mappedStatement;
        this.configuration = configuration;
        this.parameterObject = parameterObject;
    }

    @Override
    public Object getParameterObject() {
        return this.parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        BoundSql boundSql = this.mappedStatement.getSqlSource().getBoundSql(parameterObject);
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings == null) {
            return;
        }

        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            Object value;
            Class<?> handleClassType = Object.class;
            if (parameterObject == null) {
                value = null;
            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                value = parameterObject;
            } else if (parameterObject instanceof Map paramMap) {
                // parameterObject is a map, use the Map.get() method to get the value
                value = paramMap.get(parameterMapping.getProperty());
            } else {
                // parameterObject class is entity, we should use reflection to get the value
                value = ReflectUtil.getFieldValue(parameterObject, parameterMapping.getProperty());
            }

            if (value != null) {
                handleClassType = value.getClass();
            }
            TypeHandler handler = typeHandlerRegistry.resolve(handleClassType);
            handler.setParameter(ps, i + 1, value);
        }
    }
}
