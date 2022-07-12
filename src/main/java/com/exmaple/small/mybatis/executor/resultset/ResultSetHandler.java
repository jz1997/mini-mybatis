package com.exmaple.small.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface ResultSetHandler {
    <E> List<E> handleResultSet(Statement stmt) throws SQLException;
}
