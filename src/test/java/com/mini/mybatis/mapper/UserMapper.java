package com.mini.mybatis.mapper;

import com.mini.mybatis.entity.User;

import java.util.List;

// User Mapper For Test
public interface UserMapper {


    User selectByPrimaryKey(String id);

    List<User> selectAll(User user);
}
