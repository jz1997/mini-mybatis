package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.parsing.ParameterMapping;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoundSql {
  private final String sql;
  private final List<ParameterMapping> parameterMappings;
  private final Object parameterObject;
  private final Configuration configuration;

  public BoundSql(
      Configuration configuration,
      String sql,
      List<ParameterMapping> parameterMappings,
      Object parameterObject) {
    this.configuration = configuration;
    this.sql = sql;
    this.parameterMappings = parameterMappings;
    this.parameterObject = parameterObject;
  }
}
