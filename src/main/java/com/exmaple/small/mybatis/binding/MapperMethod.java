package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.reflection.ParamNameResolver;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.List;

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
        switch (sqlCommandType) {
            case INSERT -> {
                Object params = paramNameResolver.getNamedParams(args);
                return sqlSession.insert(statementId, params);
            }
            case DELETE -> {
                return null;
            }
            case SELECT -> {
                Class<?> returnType = method.getReturnType();
                if (List.class.isAssignableFrom(returnType)) {
                    // select list
                    Object params = this.paramNameResolver.getNamedParams(args);
                    return sqlSession.selectList(statementId, params);
                } else {
                    // select one
                    Object params = this.paramNameResolver.getNamedParams(args);
                    return sqlSession.selectOne(statementId, params);
                }
            }
            case UPDATE -> {
                return null;
            }
            default -> {
                throw new RuntimeException("Unknown sql command type: " + sqlCommandType);
            }
        }
    }
}
