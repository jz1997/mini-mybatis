package com.mini.mybatis.mapping;

import cn.hutool.core.util.ClassLoaderUtil;
import com.mini.mybatis.entity.User;
import com.mini.mybatis.mapper.UserMapper;
import com.mini.mybatis.session.SqlSession;
import com.mini.mybatis.session.SqlSessionFactory;
import com.mini.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

class ParameterNameResolverTest {

    private final static Logger logger = LoggerFactory.getLogger(ParameterNameResolverTest.class);

    @Test
    void test_parse() {
        InputStream ins = ClassLoaderUtil.getClassLoader().getResourceAsStream("mybatis-config-jdbc-simple.xml");
        Assertions.assertNotNull(ins);
        Reader reader = new InputStreamReader(ins);

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Assertions.assertNotNull(userMapper);

        User user = userMapper.findById("1");
        logger.info("Find User: {}", user.toString());
    }
}