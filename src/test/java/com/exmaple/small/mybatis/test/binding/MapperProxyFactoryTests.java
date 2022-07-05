package com.exmaple.small.mybatis.test.binding;

import cn.hutool.core.lang.ResourceClassLoader;
import com.exmaple.small.mybatis.session.SqlSession;
import com.exmaple.small.mybatis.session.SqlSessionFactory;
import com.exmaple.small.mybatis.session.SqlSessionFactoryBuilder;
import com.exmaple.small.mybatis.test.entity.User;
import com.exmaple.small.mybatis.test.mapper.UserMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MapperProxyFactoryTests {
  @Test
  public void test_newInstance() throws IOException {
    InputStream inputStream = ResourceClassLoader.getSystemResourceAsStream("mybatis-config.xml");
    assert inputStream != null;
    Reader reader = new InputStreamReader(inputStream);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    SqlSession sqlSession = sqlSessionFactory.openSession();
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.findById("1");
    System.out.println(user);
  }
}
