package com.exmaple.small.mybatis.session;

/** 默认的 SqlSession 工厂类 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
  private Configuration configuration;

  public DefaultSqlSessionFactory(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public SqlSession openSession() {
    return new DefaultSqlSession(configuration);
  }
}
