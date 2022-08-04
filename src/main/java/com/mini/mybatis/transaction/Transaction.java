package com.mini.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Transaction:
 * 事务接口
 *
 * @author jzheng
 * @since 2022/8/4 22:28
 */
public interface Transaction {
    /**
     * 获取对应的连接对象
     */
    Connection getConnection() throws SQLException;

    /**
     * 提交事务
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     */
    void rollback() throws SQLException;

    /**
     * 关闭事务
     */
    void close() throws SQLException;

    /**
     * 获取事务的超时时间
     */
    Integer getTimeout() throws SQLException;
}
