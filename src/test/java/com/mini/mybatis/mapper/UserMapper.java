package com.mini.mybatis.mapper;

import com.mini.mybatis.annotation.Param;
import com.mini.mybatis.entity.User;

import java.util.List;

// User Mapper For Test
public interface UserMapper {
    User findById(String id);

    User findByUsername(@Param("username") String username);

    User findOne(User user);

    List<User> findAll(User user);

    int insert(User user);

    int delete(@Param("id") String id);
}
