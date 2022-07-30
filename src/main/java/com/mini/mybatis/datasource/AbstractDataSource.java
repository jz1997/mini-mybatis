package com.mini.mybatis.datasource;

import com.mini.mybatis.exception.DataSourceException;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource, Cloneable, Closeable {
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new DataSourceException("暂不支持 getParentLogger 方法");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new DataSourceException("暂不支持 unwrap 方法");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new DataSourceException("暂不支持 isWrapperFor 方法");
    }
}
