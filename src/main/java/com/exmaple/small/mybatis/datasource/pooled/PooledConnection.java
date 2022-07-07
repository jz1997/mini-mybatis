package com.exmaple.small.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/** 链接池链接代理 */
public class PooledConnection implements InvocationHandler {

  private static final String closeMethodName = "close";
  private static final Class<?>[] IFACES = new Class<?>[] {Connection.class};

  protected Connection realConnection;
  protected Connection proxyConnection;
  protected PooledDataSource dataSource;

  public PooledConnection(Connection connection, PooledDataSource dataSource) {
    this.dataSource = dataSource;
    this.realConnection = connection;
    this.proxyConnection =
        (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(), IFACES, this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (closeMethodName.equals(method.getName())) {
      dataSource.pushConnection(this);
      return null;
    }
    try {
      return method.invoke(realConnection, args);
    } catch (Exception e) {
      throw new SQLException("Invoke method error.", e);
    }
  }

  public Connection getRealConnection() {
    return realConnection;
  }

  public Connection getProxyConnection() {
    return proxyConnection;
  }

  public PooledDataSource getDataSource() {
    return dataSource;
  }
}
