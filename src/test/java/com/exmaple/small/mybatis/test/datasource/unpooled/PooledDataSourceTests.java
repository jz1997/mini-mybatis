package com.exmaple.small.mybatis.test.datasource.unpooled;

import cn.hutool.core.lang.ResourceClassLoader;
import com.exmaple.small.mybatis.session.SqlSession;
import com.exmaple.small.mybatis.session.SqlSessionFactoryBuilder;
import com.exmaple.small.mybatis.test.entity.User;
import com.exmaple.small.mybatis.test.mapper.UserMapper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class PooledDataSourceTests {

  private static SqlSession sqlSession;

  @BeforeAll
  static void init() {
    InputStream inputStream =
        ResourceClassLoader.getSystemResourceAsStream("pooled/mybatis-config-pooled.xml");
    assert inputStream != null;
    InputStreamReader reader = new InputStreamReader(inputStream);
    sqlSession = new SqlSessionFactoryBuilder().build(reader).openSession();
  }

  @AfterAll
  static void close() {
    sqlSession.close();
  }

  @Test
  public void test_findById_success() {
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.findById("1");
    log.info("Find User: {}", user);
    Assertions.assertNotNull(user);
  }

  @Test
  public void test_findByUsername_success() {
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.findByUsername("admin");
    log.info("Find User: {}", user);
    Assertions.assertNotNull(user);
  }

  @Test
  public void test_async_findById_success() {
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    CountDownLatch latch = new CountDownLatch(20);
    IntStream.range(0, 40)
        .mapToObj(
            i ->
                new Thread(
                    () -> {
                      User user = userMapper.findById("1");
                      Assertions.assertNotNull(user);
                      log.info("Find User: {}", user);
                      latch.countDown();
                    }))
        .forEach(Thread::start);
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test_findOne_success() {
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User userQuery = User.builder().id("1").build();
    User user = userMapper.findOne(userQuery);
    log.info("Find User: {}", user);
    Assertions.assertNotNull(user);
  }
}
