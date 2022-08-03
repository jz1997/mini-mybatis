package com.mini.mybatis.datasource;

import com.mini.mybatis.datasource.factory.PooledDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * PooledDataSourceTests:
 * 测试连接池数据源
 *
 * @author jzheng
 * @since 2022/8/1 20:42
 */
public class PooledDataSourceTests {
    @Test
    public void test_getConnection() {
        Properties properties = new Properties();
        properties.put("driver", "com.mysql.cj.jdbc.Driver");
        properties.put("url", "jdbc:mysql://localhost:3306/mybatis-example?useUnicode=true&characterEncoding=utf8&useSSL=false");
        properties.put("username", "root");
        properties.put("password", "123456");

        PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
        pooledDataSourceFactory.setProperties(properties);
        DataSource dataSource = pooledDataSourceFactory.getDataSource();

        CountDownLatch countDownLatch = new CountDownLatch(40);

        IntStream.rangeClosed(1, 40)
                .mapToObj(i -> new Thread(() -> {
                    try {
                        Connection connection = dataSource.getConnection();
                        TimeUnit.SECONDS.sleep((long) ((Math.random() * 100) / 10));
                        connection.close();
                        countDownLatch.countDown();
                    } catch (SQLException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .forEach(Thread::start);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
