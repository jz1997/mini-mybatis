package com.mini.mybatis.executor.resultset;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public interface ResultSetHandler {
    <E> List<E> handleResultSet(Statement stmt);
}
