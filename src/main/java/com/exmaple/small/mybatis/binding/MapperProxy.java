package com.exmaple.small.mybatis.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class MapperProxy<T> implements InvocationHandler, Serializable {
  private static final long serialVersionUID = 4003385104312037415L;

  private final Map<String, String> sqlSession;
  private final Class<T> mapperInterface;

  public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // Object 方法不用进行代理
    if (Objects.equals(Object.class, method.getDeclaringClass())) {
      return method.invoke(this, args);
    } else {
      return "代理方法执行: " + sqlSession.get(mapperInterface.getName() + "." + method.getName());
    }
  }
}
