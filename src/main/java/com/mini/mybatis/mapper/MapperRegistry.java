package com.mini.mybatis.mapper;

import cn.hutool.core.lang.ClassScanner;
import com.mini.mybatis.exception.MapperRegistryException;
import com.mini.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    private final Map<Class<?>, MapperProxyFactory<?>> mapperProxyFactoryMap = new HashMap<>();

    /**
     * 只有 Mapper 类型为接口, 并且没有注册过的, 才可以进行注册, 否则抛出异常
     *
     * @param mapperClass /
     */
    public void addMapper(Class<?> mapperClass) {
        if (!mapperClass.isInterface()) {
            throw new MapperRegistryException("Mapper " + mapperClass.getName() + " 必须为接口");
        }

        if (hasMapper(mapperClass)) {
            throw new MapperRegistryException("Mapper " + mapperClass.getName() + " 已经注册了");
        }

        MapperProxyFactory<?> mapperProxyFactory = new MapperProxyFactory<>(mapperClass);
        mapperProxyFactoryMap.put(mapperClass, mapperProxyFactory);
    }

    public void addMappers(String packageName) {
        ClassScanner.scanPackage(packageName).forEach(this::addMapper);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> mapperClass, SqlSession sqlSession) {
        if (hasMapper(mapperClass)) {
            MapperProxyFactory<?> mapperProxyFactory = mapperProxyFactoryMap.get(mapperClass);
            return (T) mapperProxyFactory.newInstance(sqlSession);
        }
        throw new MapperRegistryException("Mapper " + mapperClass.getName() + " 不存在");
    }

    /**
     * 检查 Mapper 是否已经注册了
     *
     * @param mapperClass /
     * @return /
     */
    public boolean hasMapper(Class<?> mapperClass) {
        return mapperProxyFactoryMap.containsKey(mapperClass);
    }
}
