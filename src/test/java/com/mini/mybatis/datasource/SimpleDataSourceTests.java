package com.mini.mybatis.datasource;

import com.mini.mybatis.datasource.factory.SimpleDataSourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleDataSourceTests {

    private static final Logger log = LoggerFactory.getLogger(SimpleDataSourceTests.class);

    @Test
    public void test_getConnection() {
        Properties properties = new Properties();
        properties.put("driver", "com.mysql.cj.jdbc.Driver");
        properties.put("url", "jdbc:mysql://localhost:3306/mybatis-example?useUnicode=true&characterEncoding=utf8&useSSL=false");
        properties.put("username", "root");
        properties.put("password", "123456");

        SimpleDataSourceFactory simpleDataSourceFactory = new SimpleDataSourceFactory();
        simpleDataSourceFactory.setProperties(properties);
        DataSource dataSource = simpleDataSourceFactory.getDataSource();

        Set<Connection> connectionSet = IntStream.range(0, 40)
                .mapToObj(i -> {
                    try {
                        return dataSource.getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        Assertions.assertEquals(40, connectionSet.size());
        log.info("当前链接总数为: {}", connectionSet.size());

        connectionSet.forEach(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
