package com.exmaple.small.mybatis.executor.statement;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler extends BaseStatementHandler {

    public SimpleStatementHandler(
            MappedStatement ms, Object parameterObject, ResultHandler<?> resultHandler) {
        super(ms, parameterObject);
    }

    @Override
    protected Statement createStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler<E> resultHandler)
            throws SQLException {
        try {
            String sql = boundSql.getSql();
            statement.execute(sql);
            return resultSetHandler.handleResultSet(statement);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public void parameterize(Statement statement, Object parameter) throws SQLException {
        // nothing to do
    }
}
