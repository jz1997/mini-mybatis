package com.mini.mybatis.type;

import com.mini.mybatis.exception.TypeException;
import com.mini.mybatis.type.handler.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeHandlerRegistry {
    private final Map<Class<?>, TypeHandler<?>> javaTypeHandlerMap = new HashMap<>(32);
    private final Map<JdbcType, TypeHandler<?>> jdbcTypeHandlerMap = new HashMap<>(32);


    public TypeHandlerRegistry() {
        // register object type handler
        TypeHandler<Object> objectTypeHandler = new ObjectTypeHandler();
        register(Object.class, objectTypeHandler);

        // register boolean type handler
        TypeHandler<Boolean> booleanTypeHandler = new BooleanTypeHandler();
        register(boolean.class, booleanTypeHandler);
        register(Boolean.class, booleanTypeHandler);
        register(JdbcType.BOOLEAN, booleanTypeHandler);
        register(JdbcType.BIT, booleanTypeHandler);

        // register integer type handler
        TypeHandler<Integer> integerTypeHandler = new IntegerTypeHandler();
        register(int.class, integerTypeHandler);
        register(Integer.class, integerTypeHandler);
        register(JdbcType.INTEGER, integerTypeHandler);
        register(JdbcType.TINYINT, integerTypeHandler);
        register(JdbcType.SMALLINT, integerTypeHandler);

        // register long type handler
        TypeHandler<Long> longTypeHandler = new LongTypeHandler();
        register(long.class, longTypeHandler);
        register(Long.class, longTypeHandler);
        register(JdbcType.BIGINT, longTypeHandler);

        // register string type handler
        TypeHandler<String> stringTypeHandler = new StringTypeHandler();
        register(String.class, stringTypeHandler);
        register(JdbcType.CHAR, stringTypeHandler);
        register(JdbcType.NCHAR, stringTypeHandler);
        register(JdbcType.VARCHAR, stringTypeHandler);
        register(JdbcType.NVARCHAR, stringTypeHandler);
        register(JdbcType.LONGVARCHAR, stringTypeHandler);
        register(JdbcType.LONGNVARCHAR, stringTypeHandler);

        // register datetime
        TypeHandler<Date> dateTypeHandler = new DateTypeHandler();
        register(Date.class, dateTypeHandler);
        register(JdbcType.TIMESTAMP, dateTypeHandler);
        register(JdbcType.DATE, new DateOnlyTypeHandler());
        register(JdbcType.TIME, new TimeOnlyTypeHandler());

        // register local date time
        TypeHandler<LocalDateTime> localDateTimeTypeHandler = new LocalDateTimeTypeHandler();
        register(LocalDateTime.class, localDateTimeTypeHandler);

        // register local date
        TypeHandler<LocalDate> localDateTypeHandler = new LocalDateTypeHandler();
        register(LocalDate.class, localDateTypeHandler);

        // register local time
        TypeHandler<LocalTime> localTimeTypeHandler = new LocalTimeTypeHandler();
        register(LocalTime.class, localTimeTypeHandler);
    }

    public void register(Class<?> clazz, TypeHandler<?> handler) {
        javaTypeHandlerMap.put(clazz, handler);
    }

    public void register(JdbcType jdbcType, TypeHandler<?> handler) {
        jdbcTypeHandlerMap.put(jdbcType, handler);
    }

    public TypeHandler<?> resolve(Class<?> clazz) {
        TypeHandler<?> handler = javaTypeHandlerMap.get(clazz);
        if (handler == null) {
            throw new TypeException("No handler registered for java type: " + clazz);
        }
        return handler;
    }

    public TypeHandler<?> resolve(JdbcType jdbcType) {
        TypeHandler<?> handler = jdbcTypeHandlerMap.get(jdbcType);
        if (handler == null) {
            throw new TypeException("No handler registered for jdbc type: " + jdbcType);
        }
        return handler;
    }

}
