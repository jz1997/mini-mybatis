package com.mini.mybatis.session;

import cn.hutool.core.util.ClassLoaderUtil;
import com.mini.mybatis.entity.User;
import com.mini.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class SqlSessionBuilderTest {

    private final Logger logger = LoggerFactory.getLogger(SqlSessionBuilderTest.class);

    @Test
    public void test_build() {
        InputStream ins = ClassLoaderUtil.getClassLoader().getResourceAsStream("mybatis-config-jdbc-simple.xml");
        Assertions.assertNotNull(ins);
        Reader reader = new InputStreamReader(ins);

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Assertions.assertNotNull(userMapper);

        User user = userMapper.selectByPrimaryKey("1");
        logger.info("Find user: {}", user);
    }
}
