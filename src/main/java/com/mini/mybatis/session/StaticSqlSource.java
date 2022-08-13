package com.mini.mybatis.session;

import com.mini.mybatis.parsing.ParameterMapping;

import java.util.List;

public class StaticSqlSource implements SqlSource {

    private String sql;

    private List<ParameterMapping> parameterMappings;

    private Configuration configuration;

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings, Configuration configuration) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBondSql(Object parameterObject) {
        return BoundSql.builder()
                .sql(sql)
                .parameterMappings(parameterMappings)
                .configuration(configuration)
                .parameterObject(parameterObject)
                .build();
    }
}
