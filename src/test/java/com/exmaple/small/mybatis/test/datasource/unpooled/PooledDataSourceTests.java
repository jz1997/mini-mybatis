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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class PooledDataSourceTests {

  @Test
  public void test_findById_success() {
    InputStream inputStream =
        ResourceClassLoader.getSystemResourceAsStream("pooled/mybatis-config-pooled.xml");
    assert inputStream != null;
    InputStreamReader reader = new InputStreamReader(inputStream);
    SqlSession sqlSession = new SqlSessionFactoryBuilder().build(reader).openSession();
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.findById("1");
    log.info("Find User: {}", user);
    Assertions.assertNotNull(user);
  }

  @Test
  public void test_async_findById_success() {
    InputStream inputStream =
        ResourceClassLoader.getSystemResourceAsStream("pooled/mybatis-config-pooled.xml");
    assert inputStream != null;
    InputStreamReader reader = new InputStreamReader(inputStream);
    SqlSession sqlSession = new SqlSessionFactoryBuilder().build(reader).openSession();
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

    CountDownLatch latch = new CountDownLatch(20);
    IntStream.range(0, 20)
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
}
