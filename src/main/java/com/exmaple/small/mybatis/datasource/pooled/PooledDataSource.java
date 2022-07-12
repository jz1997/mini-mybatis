package com.exmaple.small.mybatis.datasource.pooled;

import com.exmaple.small.mybatis.datasource.unpooled.UnPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PooledDataSource extends UnPooledDataSource {

    private final PooledState state = new PooledState(this);
    private final long waitTimeout = 20000;

    public PooledDataSource() {
    }

    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state) {
            // rollback original connection if auto-commit is false
            if (!connection.getRealConnection().getAutoCommit()) {
                connection.getRealConnection().rollback();
            }
            // if the number of idle connections is less than this maximum number of idle connection,
            // then return this old connection to pool, else close the old connection
            state.removeActiveConnection(connection);
            if (state.canReturnIdleConnection()) {
                state.pushIdleConnection(connection);
                state.notifyAll();
                if (log.isDebugEnabled()) {
                    log.debug("Returned connection to pool: {}", connection);
                }
            } else {
                connection.getRealConnection().close();
                if (log.isDebugEnabled()) {
                    log.debug("Closed connection: {}", connection);
                }
            }
        }
    }

    public PooledConnection popConnection() throws SQLException {
        PooledConnection connection = null;
        while (connection == null) {
            synchronized (state) {
                // 存在空闲链接
                if (state.hasIdleConnection()) {
                    connection = state.getFirstIdleConnection();
                    if (log.isDebugEnabled()) {
                        log.debug("Checkout connection: {}.", connection);
                    }
                }
                // 不存在空闲连接
                else {
                    // 可以创建新链接
                    if (state.canCreateConnection()) {
                        connection = new PooledConnection(super.getConnection(), this);
                        if (log.isDebugEnabled()) {
                            log.debug("Create new connection: {}.", connection);
                        }
                    }
                    // 不可以创建新链接, 等待别的链接释放
                    else {
                        if (log.isDebugEnabled()) {
                            log.debug("Wait for connection release");
                        }
                        try {
                            state.wait(waitTimeout);
                        } catch (InterruptedException e) {
                            log.error("Wait for connection release error", e);
                            break;
                        }
                    }
                }

                // Get connection from pool
                if (connection != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Get connection from pool: {}.", connection);
                    }
                    // If auto commit is false, set it to rollback
                    if (!connection.realConnection.getAutoCommit()) {
                        connection.realConnection.rollback();
                    }

                    // Add connection into active pool
                    state.pushActiveConnection(connection);
                }
            }
        }

        // Get connection from pool failed
        if (connection == null) {
            if (log.isDebugEnabled()) {
                log.debug("Get connection from pool failed.");
            }
            throw new SQLException("Get connection from pool failed.");
        }

        return connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection().getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        super.setUsername(username);
        super.setPassword(password);
        return popConnection().getProxyConnection();
    }
}
