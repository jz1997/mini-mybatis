package com.exmaple.small.mybatis.transaction;

import java.sql.Connection;
import java.util.*;
import javax.sql.DataSource;

public interface TransactionFactory {
    void setProperties(Properties properties);

    Transaction newTransaction(Connection conn);

    Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit);
}
