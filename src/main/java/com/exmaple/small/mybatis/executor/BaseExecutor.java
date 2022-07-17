package com.exmaple.small.mybatis.executor;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public abstract class BaseExecutor implements Executor {

  protected Configuration configuration;

  protected Transaction transaction;

  protected Executor executor;

  protected boolean closed;

  public BaseExecutor(Configuration configuration, Transaction transaction) {
    this.configuration = configuration;
    this.transaction = transaction;
    this.executor = this;
    this.closed = false;
  }

  @Override
  public <E> List<E> query(
          MappedStatement ms, Object parameter, ResultHandler<E> resultHandler, BoundSql boundSql)
          throws SQLException {
    if (closed) {
      throw new RuntimeException("Executor is closed.");
    }
    return doQuery(ms, parameter, resultHandler, boundSql);
  }

  @Override
  public int insert(MappedStatement ms, Object parameter) throws SQLException {
    if (closed) {
      throw new RuntimeException("Executor is closed.");
    }
    return doInsert(ms, parameter);
  }

  public abstract <E> List<E> doQuery(
          MappedStatement ms, Object parameter, ResultHandler<E> resultHandler, BoundSql boundSql)
          throws SQLException;

  public abstract int doInsert(MappedStatement ms, Object parameter) throws SQLException;

  @Override
  public Transaction getTransaction() {
    if (closed) {
      throw new RuntimeException("Executor is closed.");
    }
    return transaction;
  }

  @Override
  public void commit(boolean required) throws SQLException {
    if (closed) {
      throw new RuntimeException("Cannot commit, Transaction is closed.");
    }

    if (required) {
      transaction.commit();
    }
  }

  @Override
  public void rollback(boolean required) throws SQLException {
    if (closed) {
      throw new RuntimeException("Cannot rollback, Transaction is closed.");
    }

    if (required) {
      transaction.rollback();
    }
  }

  @Override
  public void close(boolean forceRollback) {
    try {
      try {
        rollback(forceRollback);
      } finally {
        if (transaction != null) {
          transaction.close();
        }
      }
    } catch (SQLException e) {
      log.warn("Error closing transaction, Cause: ", e);
    } finally {
      closed = true;
    }
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

  protected synchronized void closeStatement(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
        transaction.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
