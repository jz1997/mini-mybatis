package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler {

  @Override
  public void setParameter(PreparedStatement ps, int index, Object parameter) throws SQLException {
    ps.setString(index, (String) parameter);
  }
}
