package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MapperRegistry;
import java.util.*;

/** 默认的 SqlSession 工厂类 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
  private MapperRegistry mapperRegistry;

  public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
    this.mapperRegistry = mapperRegistry;
  }

  @Override
  public SqlSession openSession() {
    return new DefaultSqlSession(mapperRegistry);
  }
}
