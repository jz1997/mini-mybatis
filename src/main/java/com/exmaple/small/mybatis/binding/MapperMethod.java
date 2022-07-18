package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.reflection.ParamNameResolver;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {
    private SqlCommandType sqlCommandType;
    private String statementId;
    private Method method;

    private ParamNameResolver paramNameResolver;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.statementId = mapperInterface.getName() + "." + method.getName();
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        this.sqlCommandType = mappedStatement.getSqlCommandType();
        this.method = method;
        this.paramNameResolver = new ParamNameResolver(method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        return sqlCommandType.execute(sqlSession, method, statementId, args);
    }
}
