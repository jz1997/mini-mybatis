package com.exmaple.small.mybatis.executor;

public interface ResultContext<T> {
  void stop();

  boolean isStopped();

  int getResultCount();

  T getResultObject();
}
