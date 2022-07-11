package com.exmaple.small.mybatis.test.mapper;

import com.exmaple.small.mybatis.test.entity.User;
import java.util.List;

// User Mapper For Test
public interface UserMapper {
  User findById(String id);

  User findOne(User user);

  List<User> findAll(User user);
}
