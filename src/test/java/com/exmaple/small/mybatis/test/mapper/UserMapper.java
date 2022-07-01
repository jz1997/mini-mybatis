package com.exmaple.small.mybatis.test.mapper;

import com.exmaple.small.mybatis.test.entity.User;
import java.util.List;

// User Mapper For Test
public interface UserMapper {
  User findById(String id);

  List<User> findAll(User user);
}
