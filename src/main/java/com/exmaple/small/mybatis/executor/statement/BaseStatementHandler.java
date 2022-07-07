package com.exmaple.small.mybatis.executor.statement;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.ResultHandler;
import com.exmaple.small.mybatis.executor.resultset.DefaultResultSetHandler;
import com.exmaple.small.mybatis.executor.resultset.ResultSetHandler;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.session.Configuration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler {

  protected Configuration configuration;
  protected ResultSetHandler resultSetHandler;

  protected MappedStatement mappedStatement;
  protected BoundSql boundSql;

  public BaseStatementHandler(
      MappedStatement ms, Object parameterObject, ResultHandler<?> resultHandler) {
    this.mappedStatement = ms;
    this.configuration = ms.getConfiguration();
    this.boundSql = ms.getBoundSql(parameterObject);
    this.resultSetHandler = new DefaultResultSetHandler(ms, resultHandler);
  }

  @Override
  public Statement prepare(Connection connection) throws SQLException {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      return statement;
    } catch (SQLException e) {
      closeStatement(statement);
      throw new SQLException("Error preparing statement. Cause: " + e, e);
    }
  }

  protected void closeStatement(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        // ignore exception handling
      }
    }
  }
}
