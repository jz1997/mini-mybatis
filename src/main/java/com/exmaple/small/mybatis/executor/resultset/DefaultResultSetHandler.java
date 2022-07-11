package com.exmaple.small.mybatis.executor.resultset;

import cn.hutool.core.util.ReflectUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.ResultHandler;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler {

  private MappedStatement mappedStatement;
  private ResultHandler<?> resultHandler;

  public DefaultResultSetHandler(MappedStatement mappedStatement, ResultHandler<?> resultHandler) {
    this.mappedStatement = mappedStatement;
    this.resultHandler = resultHandler;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <E> List<E> handleResultSet(Statement stmt) throws SQLException {
    try (ResultSet resultSet = stmt.getResultSet()) {
      return (List<E>) resultSetToObj(mappedStatement.getResultType(), resultSet);
    }
  }

  private List<Object> resultSetToObj(String resultType, ResultSet rs) {
    try {
      Class<?> resultClass = Class.forName(resultType);
      List<Object> objects = new ArrayList<>();
      while (rs.next()) {
        Object o = resultClass.newInstance();
        Field[] fields = ReflectUtil.getFields(resultClass);
        for (Field field : fields) {

          if (hasColumnInResultSet(field.getName(), rs)) {
            ReflectUtil.setFieldValue(o, field, rs.getObject(field.getName()));
          }
        }
        objects.add(o);
      }
      return objects;
    } catch (ClassNotFoundException
        | IllegalAccessException
        | InstantiationException
        | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean hasColumnInResultSet(String columnName, ResultSet rs) throws SQLException {
    try {
      return rs.findColumn(columnName) > 0;
    } catch (SQLException e) {
      return false;
    }
  }
}
