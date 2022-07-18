package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.Executor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

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
        return this.selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object params) {
        List<T> list = selectList(statement, params);
        if (list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            log.warn("Expected one result (or null) to be returned by selectOne, but found: {}", list);
            throw new RuntimeException(
                    "Expected one result (or null) to be returned by selectOne, but found: " + list);
        }
    }

    @Override
    public int insert(String statement) {
        return this.insert(statement, null);
    }

    @Override
    public int insert(String statement, Object params) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.insert(ms, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(String statement) {
        return this.update(statement, null);
    }

    @Override
    public int update(String statementId, Object params) {
        MappedStatement ms = configuration.getMappedStatement(statementId);
        try {
            return executor.update(ms, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(String statementId) {
        return this.delete(statementId, null);
    }

    @Override
    public int delete(String statementId, Object params) {
        MappedStatement ms = configuration.getMappedStatement(statementId);
        try {
            return executor.delete(ms, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> selectList(String statement, Object params) {
        MappedStatement ms = configuration.getMappedStatement(statement);
        try {
            return (List<T>) executor.<T>query(ms, params, Executor.EMPTY_RESULT_HANDLER, ms.getBoundSql(params));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
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
