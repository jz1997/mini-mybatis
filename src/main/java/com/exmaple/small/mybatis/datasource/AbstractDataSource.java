package com.exmaple.small.mybatis.datasource;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * DataSource 抽象实现
 */
public abstract class AbstractDataSource implements DataSource {

    public AbstractDataSource() {
    }

    @Override
    public PrintWriter getLogWriter() {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter writer) {
        DriverManager.setLogWriter(writer);
    }

    @Override
    public void setLoginTimeout(int seconds) {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException("Method 'unwrap' was not support.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        throw new UnsupportedOperationException("Method 'isWrapperFor' was not support.");
    }

    @Override
    public Logger getParentLogger() {
        throw new UnsupportedOperationException("Method 'getParentLogger' was not support.");
    }
}
