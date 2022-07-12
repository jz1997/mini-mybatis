package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> proxy = new MapperProxy<>(sqlSession, mapperInterface);
        return (T)
                Proxy.newProxyInstance(
                        mapperInterface.getClassLoader(), new Class[]{mapperInterface}, proxy);
    }
}
