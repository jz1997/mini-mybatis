package com.exmaple.small.mybatis.executor.statement;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.parameter.DefaultParameterHandler;
import com.exmaple.small.mybatis.executor.parameter.ParameterHandler;
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
    protected ParameterHandler parameterHandler;

    protected MappedStatement mappedStatement;
    protected BoundSql boundSql;

    public BaseStatementHandler(
            MappedStatement ms, Object parameterObject) {
        this.mappedStatement = ms;
        this.configuration = ms.getConfiguration();
        this.boundSql = ms.getBoundSql(parameterObject);
        this.parameterHandler =
                new DefaultParameterHandler(mappedStatement, configuration, parameterObject);
        this.resultSetHandler = new DefaultResultSetHandler(ms);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = createStatement(connection);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw new SQLException("Error preparing statement. Cause: " + e, e);
        }
    }

    protected abstract Statement createStatement(Connection connection) throws SQLException;

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
