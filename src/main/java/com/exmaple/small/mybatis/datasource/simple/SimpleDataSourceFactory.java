package com.exmaple.small.mybatis.datasource.simple;

import com.exmaple.small.mybatis.datasource.DataSourceFactory;

import java.util.*;
import javax.sql.DataSource;

public class SimpleDataSourceFactory implements DataSourceFactory {

    private SimpleDataSource simpleDataSource;

    public SimpleDataSourceFactory() {
        this.simpleDataSource = new SimpleDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        String driver =
                Optional.ofNullable(properties.getProperty("driver"))
                        .orElseThrow(() -> new RuntimeException("driver is required"));
        String url =
                Optional.ofNullable(properties.getProperty("url"))
                        .orElseThrow(() -> new RuntimeException("url is required"));
        String username =
                Optional.ofNullable(properties.getProperty("username"))
                        .orElseThrow(() -> new RuntimeException("username is required"));
        String password =
                Optional.ofNullable(properties.getProperty("password"))
                        .orElseThrow(() -> new RuntimeException("password is required"));

        simpleDataSource.setDriver(driver);
        simpleDataSource.setUrl(url);
        simpleDataSource.setUsername(username);
        simpleDataSource.setPassword(password);
    }

    @Override
    public DataSource getDataSource() {
        return simpleDataSource;
    }
}
