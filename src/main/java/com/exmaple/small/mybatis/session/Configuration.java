package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.binding.MapperRegistry;
import com.exmaple.small.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.exmaple.small.mybatis.datasource.simple.SimpleDataSourceFactory;
import com.exmaple.small.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import com.exmaple.small.mybatis.mapping.Environment;
import com.exmaple.small.mybatis.transaction.JdbcTransactionFactory;
import com.exmaple.small.mybatis.type.TypeAliasRegistry;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

  protected MapperRegistry mapperRegistry = new MapperRegistry();

  protected Map<String, MappedStatement> mappedStatements = new HashMap<>();

  protected TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

  protected Environment environment;

  public Configuration() {
    typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

    // register datasource factory
    typeAliasRegistry.registerAlias("SIMPLE", SimpleDataSourceFactory.class);
    typeAliasRegistry.registerAlias("UNPOOLED", UnPooledDataSourceFactory.class);
    typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
  }

  public Configuration(Environment environment) {
    this();
    this.environment = environment;
  }

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

  public TypeAliasRegistry getTypeAliasRegistry() {
    return typeAliasRegistry;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
