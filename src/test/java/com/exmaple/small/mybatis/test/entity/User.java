package com.exmaple.small.mybatis.test.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {
  private String id;

  private String username;

  private String nickname;

  private String password;

  private LocalDateTime createdTime;

  private LocalDateTime updatedTime;
}
