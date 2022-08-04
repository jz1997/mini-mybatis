package com.mini.mybatis.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    private static final Logger log = LoggerFactory.getLogger(JdbcTransaction.class);

    /**
     * 数据库连接
     */
    protected Connection connection;

    /**
     * 数据源
     */
    protected DataSource dataSource;

    /**
     * 事务隔离级别
     */
    protected TransactionIsolationLevel level;

    /**
     * 是否自动提交事务
     */
    protected boolean autoCommit;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        log.debug("Open JDBC Connection");
        if (connection == null) {
            connection = dataSource.getConnection();
            if (level != null) {
                connection.setTransactionIsolation(level.getLevel());
            }
            if (connection.getAutoCommit() != autoCommit) {
                connection.setAutoCommit(autoCommit);
            }
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            log.debug("Committing JDBC Connection [{}]", connection);
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            log.debug("Rolling back JDBC Connection [{}]", connection);
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            log.debug("Closing JDBC Connection [{}]", connection);
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }
}
