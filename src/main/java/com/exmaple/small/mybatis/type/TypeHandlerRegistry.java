package com.exmaple.small.mybatis.type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerRegistry {
    // TypeHandler.class -> TypeHandler Reference
    private final Map<Class<?>, TypeHandler> TYPE_HANDLER_MAP = new ConcurrentHashMap<>();
    private final ObjectTypeHandler defaultTypeHandler = new ObjectTypeHandler();

    public TypeHandlerRegistry() {
        register(String.class, new StringTypeHandler());
        register(Integer.class, new IntegerTypeHandler());
        register(Object.class, new ObjectTypeHandler());
    }

    public void register(Class<?> clazz, TypeHandler handler) {
        TYPE_HANDLER_MAP.put(clazz, handler);
    }

    public TypeHandler resolve(Class<?> clazz) {
        return TYPE_HANDLER_MAP.getOrDefault(clazz, defaultTypeHandler);
    }

    public boolean hasTypeHandler(Class<?> clazz) {
        return TYPE_HANDLER_MAP.containsKey(clazz);
    }
}
