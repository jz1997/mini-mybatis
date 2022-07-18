package com.exmaple.small.mybatis.executor.statement;

import com.exmaple.small.mybatis.executor.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {
    Statement prepare(Connection connection) throws SQLException;

    void parameterize(Statement statement, Object parameter) throws SQLException;

    <E> List<E> query(Statement statement, ResultHandler<E> resultHandler) throws SQLException;

    int insert(Statement statement) throws SQLException;

    int update(Statement statement) throws SQLException;

    int delete(Statement statement) throws SQLException;
}
