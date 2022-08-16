package com.mini.mybatis.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;

    private String username;

    private String nickname;

    private String password;
}
