package com.mini.mybatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public interface TransactionFactory {
    default void setProperties(Properties properties) {
        // Nothing to do
    }

    Transaction newTransaction(Connection conn);

    Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit);
}
