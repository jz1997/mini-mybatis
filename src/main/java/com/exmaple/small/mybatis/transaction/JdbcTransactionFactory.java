package com.exmaple.small.mybatis.transaction;

import java.sql.Connection;
import java.util.*;
import javax.sql.DataSource;

public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(
            DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(ds, level, autoCommit);
    }
}
