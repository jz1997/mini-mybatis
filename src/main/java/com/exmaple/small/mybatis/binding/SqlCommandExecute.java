package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.reflection.ParamNameResolver;
import com.exmaple.small.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public interface SqlCommandExecute {
    default Object execute(SqlSession sqlSession, Method method, String statement, Object[] args) {
        ParamNameResolver paramNameResolver = new ParamNameResolver(method);
        Object parameter = paramNameResolver.getNamedParams(args);
        return doExecute(sqlSession, method, statement, parameter);
    }

    Object doExecute(SqlSession sqlSession, Method method, String statement, Object parameter);
}
