package com.mini.mybatis.session;

import com.mini.mybatis.parsing.ParameterMapping;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BoundSql {
    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Object parameterObject;
    private Configuration configuration;
}
