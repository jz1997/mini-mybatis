package com.mini.mybatis.mapper;

import com.mini.mybatis.mapping.ParameterNameResolver;
import com.mini.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy<T> implements InvocationHandler {

    private final Class<T> mapperClass;

    private final SqlSession sqlSession;

    public MapperProxy(Class<T> mapperClass, SqlSession sqlSession) {
        this.mapperClass = mapperClass;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            // todo: 后续修改为根据 操作类型实现不同的操作
            ParameterNameResolver parameterNameResolver = new ParameterNameResolver(method);
            return sqlSession.selectOne(mapperClass.getName() + "." + method.getName(), parameterNameResolver.parse(args));
        }
    }
}
