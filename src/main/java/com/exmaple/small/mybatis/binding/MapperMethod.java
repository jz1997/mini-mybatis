package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {
    private final SqlCommandType sqlCommandType;
    private final String statementId;
    private final Method method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.statementId = mapperInterface.getName() + "." + method.getName();
        MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
        this.sqlCommandType = mappedStatement.getSqlCommandType();
        this.method = method;
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        return sqlCommandType.execute(sqlSession, method, statementId, args);
    }
}
