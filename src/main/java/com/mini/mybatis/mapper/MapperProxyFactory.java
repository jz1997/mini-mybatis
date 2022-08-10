package com.mini.mybatis.mapper;

import com.mini.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {
    private Class<T> mapperClass;

    public MapperProxyFactory(Class<T> mapperClass) {
        this.mapperClass = mapperClass;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy<>(mapperClass, sqlSession);
        return newInstance(mapperProxy);
    }
}
