package com.exmaple.small.mybatis.test.binding;

import com.exmaple.small.mybatis.binding.MapperRegistry;
import com.exmaple.small.mybatis.session.DefaultSqlSessionFactory;
import com.exmaple.small.mybatis.session.SqlSession;
import com.exmaple.small.mybatis.session.SqlSessionFactory;
import com.exmaple.small.mybatis.test.mapper.UserMapper;
import org.junit.jupiter.api.Test;

public class MapperProxyFactoryTests {
  @Test
  public void test_newInstance() {
    MapperRegistry mapperRegistry = new MapperRegistry();
    mapperRegistry.addMappers("com.exmaple.small.mybatis.test.mapper");

    SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
    SqlSession sqlSession = sqlSessionFactory.openSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    String result = mapper.findAll();
    System.out.println(result);
  }
}
