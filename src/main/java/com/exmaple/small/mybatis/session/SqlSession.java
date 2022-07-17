package com.exmaple.small.mybatis.session;

import java.sql.SQLException;
import java.util.List;

public interface SqlSession {
    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object params);

    int insert(String statement);

    int insert(String statement, Object params);

    <T> List<T> selectList(String statement, Object parameter);


    Configuration getConfiguration();

    <T> T getMapper(Class<T> type);

    void close();
}
