package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MapperRegistry;
import java.util.*;

/** 默认 SqlSession 实现 */
public class DefaultSqlSession implements SqlSession {

  /** */
  public MapperRegistry mapperRegistry;

  public DefaultSqlSession(MapperRegistry mapperRegistry) {
    this.mapperRegistry = mapperRegistry;
  }

  @Override
  public <T> T selectOne(String statement) {
    return (T) String.format("selectOne(%s)", statement);
  }

  @Override
  public <T> T selectOne(String statement, Object params) {
    return (T) String.format("selectOne(%s, %s)", statement, params);
  }

  @Override
  public <T> T getMapper(Class<T> type) {
    return mapperRegistry.getMapper(type, this);
  }
}
