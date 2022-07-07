package com.exmaple.small.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PooledState {
  protected PooledDataSource dataSource;
  protected final List<PooledConnection> idleConnections = new ArrayList<>();
  protected final List<PooledConnection> activeConnections = new ArrayList<>();
  protected int maxActiveConnections = 10;
  protected int maxIdleConnections = 5;

  public PooledState(PooledDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void pushIdleConnection(PooledConnection connection) {
    idleConnections.add(connection);
  }

  public void removeIdleConnection(PooledConnection connection) {
    idleConnections.remove(connection);
  }

  public void pushActiveConnection(PooledConnection connection) {
    activeConnections.add(connection);
  }

  public void removeActiveConnection(PooledConnection connection) {
    activeConnections.remove(connection);
  }

  public boolean hasIdleConnection() {
    return !this.idleConnections.isEmpty();
  }

  public PooledConnection getFirstIdleConnection() {
    return this.idleConnections.remove(0);
  }

  public boolean canCreateConnection() {
    return this.activeConnections.size() < this.maxActiveConnections;
  }

  public boolean canReturnIdleConnection() {
    return this.idleConnections.size() < this.maxIdleConnections;
  }
}
