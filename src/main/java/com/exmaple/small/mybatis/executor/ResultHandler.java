package com.exmaple.small.mybatis.executor;

public interface ResultHandler<T> {
  void handleResult(ResultContext<? extends T> context);
}
