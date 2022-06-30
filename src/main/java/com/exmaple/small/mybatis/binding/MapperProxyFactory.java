package com.exmaple.small.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

public class MapperProxyFactory<T> {
  private final Class<T> mapperInterface;

  public MapperProxyFactory(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }

  @SuppressWarnings("unchecked")
  public T newInstance(Map<String, String> sqlSession) {
    MapperProxy<T> proxy = new MapperProxy<>(sqlSession, mapperInterface);
    return (T)
        Proxy.newProxyInstance(
            mapperInterface.getClassLoader(), new Class[] {mapperInterface}, proxy);
  }
}
