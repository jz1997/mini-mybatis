package com.exmaple.small.mybatis.session;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
