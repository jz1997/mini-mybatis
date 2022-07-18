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
        super(ms, parameterObject);
    }

    @Override
    protected Statement createStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler<E> resultHandler)
            throws SQLException {
        String sql = boundSql.getSql();
        log.info("PrepareStatementHandler.query: sql={}", sql);
        ((PreparedStatement) statement).execute();
        return resultSetHandler.handleResultSet(statement);
    }

    @Override
    public int insert(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        String sql = boundSql.getSql();
        log.info("PrepareStatementHandler.insert: sql={}", sql);
        ps.execute();
        return ps.getUpdateCount();
    }

    @Override
    public int update(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        String sql = boundSql.getSql();
        log.info("PrepareStatementHandler.update: sql={}", sql);
        ps.execute();
        return ps.getUpdateCount();
    }

    @Override
    public int delete(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        String sql = boundSql.getSql();
        log.info("PrepareStatementHandler.delete: sql={}", sql);
        ps.execute();
        return statement.getUpdateCount();
    }

    @Override
    public void parameterize(Statement statement, Object parameter) throws SQLException {
        parameterHandler.setParameters((PreparedStatement) statement);
    }
}
