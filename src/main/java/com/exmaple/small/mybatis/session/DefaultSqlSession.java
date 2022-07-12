package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.Executor;

import java.sql.SQLException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认 SqlSession 实现
 */
@Slf4j
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T selectOne(String statement, Object params) {
        MappedStatement ms = configuration.getMappedStatement(statement);
        try {
            List<T> list =
                    executor.<T>query(ms, params, Executor.EMPTY_RESULT_HANDLER, ms.getBoundSql(params));
            if (list.size() == 0) {
                return null;
            } else if (list.size() == 1) {
                return list.get(0);
            } else {
                log.warn("Expected one result (or null) to be returned by selectOne, but found: {}", list);
                throw new RuntimeException(
                        "Expected one result (or null) to be returned by selectOne, but found: " + list);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public void close() {
        executor.close(false);
    }
}
