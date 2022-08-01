package com.mini.mybatis.datasource.connection;

import cn.hutool.db.DbUtil;
import com.mini.mybatis.datasource.PooledDataSource;
import com.mini.mybatis.exception.DataSourceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PooledConnection extends ConnectionWrapper {
    private PooledDataSource dataSource;
    private boolean isClosed;

    public PooledConnection(PooledDataSource dataSource, Connection connection) {
        this.dataSource = dataSource;
        this.isClosed = false;
        this.realConnection = connection;
    }

    public void open() {
        this.isClosed = false;
    }

    /**
     * 关闭 Connection 连接
     */
    public void release() {
        DbUtil.close(this.realConnection);
    }

    /**
     * 重写 close 方法, 将本链接防止到数据源中的连接池中
     *
     * @throws SQLException /
     */
    @Override
    public void close() throws SQLException {
        // 连接关闭放回连接池中
        this.dataSource.free(this);
        this.isClosed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return isClosed || realConnection.isClosed();
    }

}
