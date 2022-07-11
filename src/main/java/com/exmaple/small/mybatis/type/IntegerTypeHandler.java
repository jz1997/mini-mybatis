package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerTypeHandler implements TypeHandler {
  @Override
  public void setParameter(PreparedStatement ps, int index, Object parameter) throws SQLException {
    ps.setInt(index, (Integer) parameter);
  }
}
