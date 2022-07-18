package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.List;

public enum SqlCommandType implements SqlCommandExecute {
    SELECT {
        @Override
        public Object doExecute(SqlSession sqlSession, Method method, String statement, Object parameter) {
            Class<?> returnType = method.getReturnType();
            if (List.class.isAssignableFrom(returnType)) {
                return sqlSession.selectList(statement, parameter);
            } else {
                return sqlSession.selectOne(statement, parameter);
            }
        }
    },
    INSERT {
        @Override
        public Object doExecute(SqlSession sqlSession, Method method, String statement, Object parameter) {
            return sqlSession.insert(statement, parameter);
        }
    },
    UPDATE {
        @Override
        public Object doExecute(SqlSession sqlSession, Method method, String statement, Object parameter) {
            return sqlSession.update(statement, parameter);
        }
    },
    DELETE {
        @Override
        public Object doExecute(SqlSession sqlSession, Method method, String statement, Object parameter) {
            return sqlSession.delete(statement, parameter);
        }
    }
}
