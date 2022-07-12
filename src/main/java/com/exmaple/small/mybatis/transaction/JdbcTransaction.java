package com.exmaple.small.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class JdbcTransaction implements Transaction {

    private static final Logger log = LoggerFactory.getLogger(JdbcTransaction.class);

    /**
     *
     */
    private Connection connection;

    /**
     *
     */
    private DataSource dataSource;

    /**
     *
     */
    private TransactionIsolationLevel level;

    /**
     *
     */
    private boolean autocommit;

    public JdbcTransaction(
            DataSource dataSource, TransactionIsolationLevel level, boolean autocommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autocommit = autocommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // open connection
        if (connection == null) {
            connection = dataSource.getConnection();
        }

        // set isolation level
        if (level != null) {
            connection.setTransactionIsolation(level.getLevel());
        }

        // set autocommit
        try {
            if (connection.getAutoCommit() != autocommit) {
                connection.setAutoCommit(autocommit);
            }
        } catch (SQLException e) {
            log.error(
                    "Your database driver may not support setAutoCommit() or getAutoCommit(). Requested setting {}. Cause: {}",
                    autocommit,
                    e);
            throw new SQLException(e);
        }

        return connection;
    }

    /**
     *
     */
    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    /**
     *
     */
    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }

    /**
     *
     */
    @Override
    public synchronized void close() throws SQLException {
        if (connection != null) {
            resetAutoCommit();
            connection.close();
            connection = null;
        }
    }

    private void resetAutoCommit() throws SQLException {
        if (!connection.getAutoCommit()) {
            connection.setAutoCommit(true);
        }
    }
}
