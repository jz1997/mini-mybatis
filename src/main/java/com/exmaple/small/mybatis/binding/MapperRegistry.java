package com.exmaple.small.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import com.exmaple.small.mybatis.session.SqlSession;

import java.util.*;

/**
 * Mappers 注册中心
 */
public class MapperRegistry {

    // 已经注册的 MapperProxyFactory
    private final Map<Class<?>, MapperProxyFactory<?>> mappers = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        // Get Mapper Proxy Factory
        MapperProxyFactory<T> mapperProxyFactory =
                (MapperProxyFactory<T>)
                        Optional.ofNullable(mappers.get(type))
                                .orElseThrow(() -> new IllegalArgumentException("Unknown mapper type: " + type));

        // Create Mapper Instance
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Create mapper instance fail. Mapper type: " + type + ". Cause: " + e);
        }
    }

    public <T> void addMapper(Class<T> type) {
        // Mapper 是接口才可以注册
        if (!type.isInterface()) {
            return;
        }

        // 已经注册了
        if (mappers.containsKey(type)) {
            throw new IllegalArgumentException("Mapper type: " + type + " has been registered.");
        }

        // 注册 Mapper Proxy Factory
        MapperProxyFactory<T> mapperProxyFactory = new MapperProxyFactory<>(type);
        this.mappers.put(type, mapperProxyFactory);
    }

    // 通过 ClassScanner 扫描所有的 Mapper 接口
    public void addMappers(String packageName) {
        ClassScanner.scanPackage(packageName).forEach(this::addMapper);
    }
}
