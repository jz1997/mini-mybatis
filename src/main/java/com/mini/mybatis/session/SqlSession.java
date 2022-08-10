package com.mini.mybatis.session;

import java.util.List;

public interface SqlSession {
    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <E> List<E> selectList(String statement);

    <E> List<E> selectList(String statement, Object parameter);

    <T> T getMapper(Class<T> mapperClass);

    Configuration getConfiguration();
}
