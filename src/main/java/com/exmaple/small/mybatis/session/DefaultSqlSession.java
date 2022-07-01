package com.exmaple.small.mybatis.session;

import lombok.extern.slf4j.Slf4j;

/** 默认 SqlSession 实现 */
@Slf4j
public class DefaultSqlSession implements SqlSession {

  private Configuration configuration;

  public DefaultSqlSession(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public <T> T selectOne(String statement) {
    return (T) String.format("selectOne(%s)", statement);
  }

  @Override
  public <T> T selectOne(String statement, Object params) {
    // TODO: IMPLEMENT TO QUERY DATA FROM DATABASE
    log.info("代理方法({}, {})", statement, params);
    log.info(configuration.getMappedStatement(statement).getSqlSource().getSql());
    return (T) null;
  }

  @Override
  public <T> T getMapper(Class<T> type) {
    return configuration.getMapper(type, this);
  }
}
