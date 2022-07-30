package com.mini.mybatis.datasource.connection;

import com.mini.mybatis.datasource.PooledDataSource;
import com.mini.mybatis.exception.DataSourceException;

import java.sql.SQLException;

public class PooledConnection extends ConnectionWrapper {
    private PooledDataSource dataSource;
    private boolean isClosed;

    public PooledConnection(PooledDataSource dataSource) {
        this.dataSource = dataSource;
        this.isClosed = false;
        try {
            super.realConnection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataSourceException("获取数据源链接失败, 原因: " + e.getMessage(), e);
        }
    }


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

    public PooledConnection open() {
        this.isClosed = false;
        return this;
    }
}
