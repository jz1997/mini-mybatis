package com.exmaple.small.mybatis.executor.statement;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.ResultHandler;
import lombok.SneakyThrows;

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
        String sql = boundSql.getSql();
        statement.execute(sql);
        return resultSetHandler.handleResultSet(statement);
    }

    @Override
    public int insert(Statement statement) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return statement.getUpdateCount();
    }

    @Override
    public int update(Statement statement) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return statement.getUpdateCount();
    }

    @Override
    public int delete(Statement statement) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return statement.getUpdateCount();
    }

    @Override
    public void parameterize(Statement statement, Object parameter) throws SQLException {
        // nothing to do
    }
}
