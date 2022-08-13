package com.mini.mybatis.session;

public interface  SqlSource {
    BoundSql getBondSql(Object parameterObject);
}
