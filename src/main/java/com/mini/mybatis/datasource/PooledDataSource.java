package com.mini.mybatis.datasource;

import com.mini.mybatis.datasource.connection.PooledConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;

public class PooledDataSource extends AbstractDataSource {

    @Override
    public void close() throws IOException {

    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    public void free(PooledConnection pooledConnection) {

    }
}
