package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.binding.MapperRegistry;
import java.util.*;

public class Configuration {

  protected MapperRegistry mapperRegistry = new MapperRegistry();

  protected Map<String, MappedStatement> mappedStatements = new HashMap<>();

  public Configuration() {}

  public <T> void addMapper(Class<T> type) {
    mapperRegistry.addMapper(type);
  }

  public void addMappers(String packageName) {
    mapperRegistry.addMappers(packageName);
  }

  public void addMappedStatement(MappedStatement ms) {
    mappedStatements.put(ms.getId(), ms);
  }

  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    return mapperRegistry.getMapper(type, sqlSession);
  }

  public MappedStatement getMappedStatement(String id) {
    return mappedStatements.get(id);
  }
}
