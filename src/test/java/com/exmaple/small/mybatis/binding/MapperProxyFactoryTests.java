package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.binding.mapper.UserMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class MapperProxyFactoryTests {
  @Test
  public void test_newInstance() {
    Map<String, String> sqlSession = new HashMap<>();
    sqlSession.put(
        "com.exmaple.small.mybatis.binding.mapper.UserMapper.findById", "代理方法执行: findById()");
    sqlSession.put(
        "com.exmaple.small.mybatis.binding.mapper.UserMapper.findAll", "代理方法执行: findAll()");

    MapperProxyFactory<UserMapper> proxyFactory = new MapperProxyFactory<>(UserMapper.class);

    UserMapper userMapper = proxyFactory.newInstance(sqlSession);

    System.out.println(userMapper.findById("1"));
    System.out.println(userMapper.findAll());
  }
}
