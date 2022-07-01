package com.exmaple.small.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SqlSource {

  /** */
  private String sql;

  public String getSql() {
    return sql;
  }
}
