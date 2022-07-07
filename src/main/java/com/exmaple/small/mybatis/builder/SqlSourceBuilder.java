package com.exmaple.small.mybatis.builder;

import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSource;
import com.exmaple.small.mybatis.session.StaticSqlSource;
import java.util.ArrayList;

public class SqlSourceBuilder extends BasicBuilder {

  public SqlSourceBuilder(Configuration configuration) {
    super(configuration);
  }

  public SqlSource parse(String originalSql, Class<?> parameterType) {
    // TODO: parse originalSql and return SqlSource and parameterMappings
    String sql = originalSql;
    return new StaticSqlSource(sql, configuration, new ArrayList<>());
  }
}
