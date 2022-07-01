package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.session.SqlSession;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperProxy<T> implements InvocationHandler, Serializable {
  private static final long serialVersionUID = 4003385104312037415L;

  private final SqlSession sqlSession;
  private final Class<T> mapperInterface;

  public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // Object 方法不用进行代理
    if (Objects.equals(Object.class, method.getDeclaringClass())) {
      return method.invoke(this, args);
    } else {
      return sqlSession.selectOne(mapperInterface.getName() + "." + method.getName(), args);
    }
  }
}
