package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.parsing.ParameterMapping;

import java.util.List;

public class StaticSqlSource implements SqlSource {
    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final Configuration configuration;

    public StaticSqlSource(String sql, Configuration configuration) {
        this(sql, configuration, null);
    }

    public StaticSqlSource(
            String sql, Configuration configuration, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }
}
