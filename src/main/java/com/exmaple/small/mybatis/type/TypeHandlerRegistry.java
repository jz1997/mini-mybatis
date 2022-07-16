package com.exmaple.small.mybatis.type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerRegistry {
    // TypeHandler.class -> TypeHandler Reference
    private final Map<Class<?>, TypeHandler<?>> TYPE_HANDLER_MAP = new ConcurrentHashMap<>();
    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new ConcurrentHashMap<>();
    private final ObjectTypeHandler defaultTypeHandler = new ObjectTypeHandler();

    public TypeHandlerRegistry() {

        // Boolean
        BooleanTypeHandler booleanTypeHandler = new BooleanTypeHandler();
        register(Boolean.class, booleanTypeHandler);
        register(JdbcType.BOOLEAN, booleanTypeHandler);
        register(JdbcType.TINYINT, booleanTypeHandler);

        // Integer
        IntegerTypeHandler integerTypeHandler = new IntegerTypeHandler();
        register(Integer.class, integerTypeHandler);
        register(JdbcType.SMALLINT, integerTypeHandler);
        register(JdbcType.INTEGER, integerTypeHandler);

        // LONG
        LongTypeHandler longTypeHandler = new LongTypeHandler();
        register(Long.class, longTypeHandler);
        register(JdbcType.BIGINT, longTypeHandler);

        // String
        StringTypeHandler stringTypeHandler = new StringTypeHandler();
        register(String.class, stringTypeHandler);
        register(JdbcType.CHAR, stringTypeHandler);
        register(JdbcType.NCHAR, stringTypeHandler);
        register(JdbcType.VARCHAR, stringTypeHandler);
        register(JdbcType.NVARCHAR, stringTypeHandler);
        register(JdbcType.LONGVARCHAR, stringTypeHandler);
        register(JdbcType.LONGNVARCHAR, stringTypeHandler);
    }

    public void register(Class<?> clazz, TypeHandler<?> handler) {
        TYPE_HANDLER_MAP.put(clazz, handler);
    }

    public void register(JdbcType jdbcType, TypeHandler<?> handler) {
        JDBC_TYPE_HANDLER_MAP.put(jdbcType, handler);
    }

    public TypeHandler<?> resolve(Class<?> clazz) {
        return TYPE_HANDLER_MAP.getOrDefault(clazz, defaultTypeHandler);
    }

    public TypeHandler<?> resolve(JdbcType jdbcType) {
        return JDBC_TYPE_HANDLER_MAP.getOrDefault(jdbcType, defaultTypeHandler);
    }

    public boolean hasTypeHandler(Class<?> clazz) {
        return TYPE_HANDLER_MAP.containsKey(clazz);
    }
}
