package com.exmaple.small.mybatis.session;

import java.util.List;

public interface SqlSession {
    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object params);

    int insert(String statement);

    int insert(String statement, Object params);

    int update(String statement);

    int update(String statementId, Object params);

    int delete(String statementId);

    int delete(String statementId, Object params);

    <T> List<T> selectList(String statement, Object parameter);

    Configuration getConfiguration();

    <T> T getMapper(Class<T> type);

    void close();
}
