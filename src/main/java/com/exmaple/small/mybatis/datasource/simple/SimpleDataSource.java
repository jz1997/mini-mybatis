package com.exmaple.small.mybatis.datasource.simple;

import com.exmaple.small.mybatis.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDataSource extends AbstractDataSource {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataSource.class);

    private String driver;

    private String url;

    private String username;

    private String password;

    public SimpleDataSource() {
    }

    public SimpleDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class<?> driverClass = Class.forName(driver);
            DriverManager.registerDriver((Driver) driverClass.newInstance());
        } catch (ClassNotFoundException e) {
            log.error("Driver class not found: {}", driver);
            throw new RuntimeException(e);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, this.username, this.password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
}
