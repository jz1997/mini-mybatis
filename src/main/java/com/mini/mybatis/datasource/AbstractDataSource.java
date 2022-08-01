package com.mini.mybatis.datasource;

import com.mini.mybatis.exception.DataSourceException;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource, Cloneable, Closeable {
    protected static final Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();
    protected String driver;
    protected String url;
    protected String username;
    protected String password;
    protected Properties driverProperties;

    // 加载已注册驱动
    static {
        Iterator<Driver> driverIterator = DriverManager.getDrivers().asIterator();
        while (driverIterator.hasNext()) {
            Driver dr = driverIterator.next();
            registeredDrivers.put(dr.getClass().getName(), dr);
        }
    }

    /**
     * 注册数据源驱动
     */
    private void initDriver() {
        if (!registeredDrivers.containsKey(driver)) {
            try {
                Class<?> driverClass = Class.forName(driver);
                Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();
                registeredDrivers.put(driver, driverInstance);
                DriverManager.registerDriver(driverInstance);
            } catch (ClassNotFoundException e) {
                throw new DataSourceException("数据源驱动不存在, driver: " + driver + ". ", e);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(this.username, this.password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    protected Connection doGetConnection(String username, String password) throws SQLException {
        initDriver();

        Properties properties = new Properties();
        if (driverProperties != null) {
            properties.putAll(driverProperties);
        }

        if (username != null) {
            properties.put("user", username);
        }

        if (password != null) {
            properties.put("password", password);
        }

        return doGetConnection(properties);
    }


    protected abstract Connection doGetConnection(Properties properties) throws SQLException;

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
