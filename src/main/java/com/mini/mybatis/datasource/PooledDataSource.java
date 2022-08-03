package com.mini.mybatis.datasource;

import cn.hutool.core.collection.CollUtil;
import com.mini.mybatis.datasource.connection.PooledConnection;
import com.mini.mybatis.exception.DataSourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PooledDataSource extends SimpleDataSource {

    private final static Logger log = LoggerFactory.getLogger(PooledDataSource.class);

    private int poolTimeToWait = 20000;
    private int maxIdleConnections = 10;
    private int maxActiveConnections = 20;
    private int activeConnections = 0;

    /**
     * 数据源
     */
    private final Deque<PooledConnection> connectionPool = new ArrayDeque<>();

    public PooledDataSource() {
    }


    /**
     * 如果如果连接池中的空闲数量小于 maxIdleConnection 则放入连接池中, 否则关闭即可
     *
     * @param pooledConnection /
     */
    public void free(PooledConnection pooledConnection) {
        synchronized (connectionPool) {
            if (this.canOffer()) {
                connectionPool.offerLast(pooledConnection);
                log.debug("将连接 {} 放回至连接池中, 当前连接数: {}", pooledConnection, activeConnections);
                connectionPool.notifyAll();
            } else {
                pooledConnection.release();
                activeConnections--;
                log.debug("移除连接, Connection: {}, 当前连接数: {}", pooledConnection, activeConnections);
            }
        }
    }

    private boolean canOffer() {
        return this.connectionPool.size() < this.maxIdleConnections;
    }


    @Override
    protected Connection doGetConnection(Properties properties) throws SQLException {
        PooledConnection returnConnection = null;
        while (returnConnection == null) {
            synchronized (connectionPool) {
                // 存在空闲线程
                if (this.hasIdleConnection()) {
                    returnConnection = connectionPool.pollFirst();
                    log.debug("从空闲连接池中获取连接, Connection: {}", returnConnection);
                }

                // 可以创建新线程
                else if (this.canNewConnection()) {
                    returnConnection = this.newConnection(properties);
                    activeConnections++;
                    log.debug("创建新的连接, Connection: {}, 当前连接数: {}", returnConnection, activeConnections);
                }

                // 进行等待
                else {
                    try {
                        log.debug("等待连接池中的连接被释放, 等待时间: {}", poolTimeToWait);
                        connectionPool.wait(poolTimeToWait);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                // 获取到新的连接
                if (returnConnection != null) {
                    returnConnection.open();

                    if (returnConnection.isClosed()) {
                        log.debug("连接已经为关闭状态, 移除该连接: " + returnConnection);
                        throw new DataSourceException("连接已经关闭, 移除该连接: " + returnConnection);
                    }
                }
            }
        }

        if (returnConnection == null) {
            log.debug("连接池中没有空闲连接");
            throw new DataSourceException("连接池中没有可用的连接");
        }

        return returnConnection;
    }

    private boolean hasIdleConnection() {
        return CollUtil.isNotEmpty(connectionPool);
    }

    private boolean canNewConnection() {
        return activeConnections < maxActiveConnections;
    }

    private PooledConnection newConnection(Properties properties) {
        try {
            Connection connection = DriverManager.getConnection(this.url, properties);
            return new PooledConnection(this, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (!CollUtil.isEmpty(connectionPool)) {
            connectionPool.forEach(PooledConnection::release);
            connectionPool.clear();
        }
    }

}
