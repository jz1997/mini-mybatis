package com.exmaple.small.mybatis.executor.statement;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrepareStatementHandler extends BaseStatementHandler {

    public PrepareStatementHandler(
            MappedStatement ms, Object parameterObject, ResultHandler<?> resultHandler) {
        super(ms, parameterObject, resultHandler);
    }

    @Override
    protected Statement createStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler<E> resultHandler)
            throws SQLException {
        try {
            String sql = boundSql.getSql();
            log.info("PrepareStatementHandler.query: sql={}", sql);
            ((PreparedStatement) statement).execute();
            return resultSetHandler.handleResultSet(statement);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public void parameterize(Statement statement, Object parameter) throws SQLException {
        parameterHandler.setParameters((PreparedStatement) statement);
    }
}
