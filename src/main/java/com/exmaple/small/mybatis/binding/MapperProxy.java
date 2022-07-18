package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.reflection.ParamNameResolver;
import com.exmaple.small.mybatis.session.SqlSession;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperProxy<T> implements InvocationHandler, Serializable {
    @Serial
    private static final long serialVersionUID = 4003385104312037415L;

    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Objects.equals(Object.class, method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            MapperMethod mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
            return mapperMethod.execute(sqlSession, args);
        }
    }
}
