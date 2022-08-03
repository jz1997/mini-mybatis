package com.mini.mybatis.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SimpleDataSource extends AbstractDataSource {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataSource.class);

    /**
     * 实际获取连接的方法
     *
     * @param properties 数据源配置
     * @return /
     * @throws SQLException /
     */
    @Override
    protected Connection doGetConnection(Properties properties) throws SQLException {
        Connection connection = DriverManager.getConnection(url, properties);
        log.debug("获取到连接: {}", connection);
        return connection;
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
