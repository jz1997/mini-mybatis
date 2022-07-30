package com.mini.mybatis.datasource;

import com.mini.mybatis.exception.DataSourceException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleDataSource extends AbstractDataSource {
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

    // 注册驱动
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

    private Connection doGetConnection(String username, String password) throws SQLException {
        initDriver();

        Properties properties = new Properties();
        if (driverProperties != null) {
            properties.putAll(driverProperties);
        }

        if (username != null) {
            properties.put("username", username);
        }

        if (password != null) {
            properties.put("password", password);
        }

        return doGetConnection(properties);
    }

    private Connection doGetConnection(Properties properties) throws SQLException {
        return DriverManager.getConnection(url, properties);
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
