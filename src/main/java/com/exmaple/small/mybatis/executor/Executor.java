package com.exmaple.small.mybatis.executor;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public interface Executor {

  ResultHandler EMPTY_RESULT_HANDLER = null;

  <E> List<E> query(
          MappedStatement ms, Object parameter, ResultHandler<E> resultHandler, BoundSql boundSql)
          throws SQLException;

  int insert(MappedStatement ms, Object parameter) throws SQLException;

  Transaction getTransaction();

  void commit(boolean required) throws SQLException;

  void rollback(boolean required) throws SQLException;

  void close(boolean forceRollback);

  boolean isClosed();
}
