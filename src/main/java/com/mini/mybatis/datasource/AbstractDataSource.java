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

public abstract class AbstractDataSource implements DataSource, Closeable {
    // 已经注册的驱动
    protected static final Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();
    // 数据源驱动
    protected String driver;
    // 数据库连接 url
    protected String url;
    // 用户名
    protected String username;
    // 密码
    protected String password;
    // 数据源额外配置
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
     * 如果当前数据源的驱动没有注册的话就注册数据源驱动
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

    /**
     * 尝试加载数据源驱动, 并将用户名、密码和数据源额外配置设置为一个 Properties 对象
     *
     * @param username 用户名
     * @param password 密码
     * @return 数据库连接
     * @throws SQLException /
     */
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


    /**
     * 实际获取Connection 的抽象方法，等待子类实现
     *
     * @param properties 数据源相关的属性, user, password 等等
     * @return {@link Connection} /
     * @throws SQLException /
     */
    protected abstract Connection doGetConnection(Properties properties) throws SQLException;

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Properties getDriverProperties() {
        return driverProperties;
    }

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
