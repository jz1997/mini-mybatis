package com.exmaple.small.mybatis.test.datasource.unpooled;

import cn.hutool.core.lang.ResourceClassLoader;
import com.exmaple.small.mybatis.datasource.unpooled.UnPooledDataSource;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSession;
import com.exmaple.small.mybatis.session.SqlSessionFactory;
import com.exmaple.small.mybatis.session.SqlSessionFactoryBuilder;
import com.exmaple.small.mybatis.test.entity.User;
import com.exmaple.small.mybatis.test.mapper.UserMapper;
import com.exmaple.small.mybatis.xml.XmlConfigBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnPooledDataSourceTests {

    private static final Logger log = LoggerFactory.getLogger(UnPooledDataSourceTests.class);

    @Test
    public void test_findById_success() {
        InputStream inputStream =
                ResourceClassLoader.getSystemResourceAsStream("unpooled/mybatis-config-unpooled.xml");
        assert inputStream != null;
        InputStreamReader reader = new InputStreamReader(inputStream);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.findById("1");
        Assertions.assertNotNull(user);
        log.info("Find User: {}", user);
    }

    @Test
    public void test_unknownProperty() {
        InputStream inputStream =
                ResourceClassLoader.getSystemResourceAsStream(
                        "unpooled/mybatis-config-unpooled-unknown-property.xml");
        assert inputStream != null;
        InputStreamReader reader = new InputStreamReader(inputStream);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new SqlSessionFactoryBuilder().build(reader);
                });
    }

    @Test
    public void test_driverProperty() {
        InputStream inputStream =
                ResourceClassLoader.getSystemResourceAsStream(
                        "unpooled/mybatis-config-unpooled-driver-property.xml");
        assert inputStream != null;
        InputStreamReader reader = new InputStreamReader(inputStream);
        Configuration configuration = new XmlConfigBuilder(reader).parse();
        UnPooledDataSource dataSource =
                (UnPooledDataSource) configuration.getEnvironment().getDataSource();
        Properties driverProperties = dataSource.getDriverProperties();
        log.info("Driver Properties: {}", driverProperties);
        Assertions.assertNotNull(driverProperties);
        Assertions.assertEquals(driverProperties.getProperty("encoding"), "utf-8");
    }
}
