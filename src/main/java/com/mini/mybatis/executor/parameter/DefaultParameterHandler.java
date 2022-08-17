package com.mini.mybatis.executor.parameter;

import cn.hutool.core.util.ReflectUtil;
import com.mini.mybatis.mapping.MappedStatement;
import com.mini.mybatis.parsing.ParameterMapping;
import com.mini.mybatis.session.BoundSql;
import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.type.TypeHandler;
import com.mini.mybatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class DefaultParameterHandler implements ParameterHandler {
    private final Configuration configuration;
    private final MappedStatement ms;
    private final Object parameterObject;

    public DefaultParameterHandler(MappedStatement ms, Object parameterObject) {
        this.configuration = ms.getConfiguration();
        this.ms = ms;
        this.parameterObject = parameterObject;
    }

    @Override
    public Object getParameterObject() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setParameters(PreparedStatement ps) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        BoundSql boundSql = ms.getSqlSource().getBondSql(parameterObject);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Class<?> handlerJavaType = Object.class;

        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping pm = parameterMappings.get(i);
            Object value;

            // 参数为 null
            if (parameterObject == null) {
                value = null;
            }

            // 存在 parameterObject 类型的转换器
            else if (typeHandlerRegistry.has(parameterObject.getClass())) {
                value = parameterObject;
            }

            // parameterObject 为 map 类型, 直接取出 property 键的值
            else if (parameterObject instanceof Map<?, ?> propertyValueMap) {
                value = propertyValueMap.get(pm.getProperty());
            }

            // 特殊对象类型, 采用反射方式进行取值
            else {
                value = ReflectUtil.getFieldValue(parameterObject, pm.getProperty());
            }


            // 根据 value 类型获取 TypeHandler, 默认为 TypeHandler<Object>
            if (value != null) {
                handlerJavaType = value.getClass();
            }


            // 使用 TypeHandler 进行填充参数值
            TypeHandler<Object> typeHandler = (TypeHandler<Object>) typeHandlerRegistry.resolve(handlerJavaType);
            typeHandler.setParameter(ps, i + 1, value, null);
        }
    }
}
