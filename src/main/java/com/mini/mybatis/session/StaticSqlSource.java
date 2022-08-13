package com.mini.mybatis.session;

import com.mini.mybatis.parsing.ParameterMapping;
import lombok.Getter;

import java.util.List;

@Getter
public class StaticSqlSource implements SqlSource {

    // 可能含有占位符的可执行 SQL 语句
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
