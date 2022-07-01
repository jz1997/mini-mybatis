package com.exmaple.small.mybatis.session;

public interface SqlSession {
  <T> T selectOne(String statement);

  <T> T selectOne(String statement, Object params);

  <T> T getMapper(Class<T> type);
}
