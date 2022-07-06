package com.exmaple.small.mybatis.session;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

/** 默认 SqlSession 实现 */
@Slf4j
public class DefaultSqlSession implements SqlSession {

  private Configuration configuration;

  public DefaultSqlSession(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public <T> T selectOne(String statement) {
    return (T) null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T selectOne(String statement, Object params) {
    MappedStatement mappedStatement = configuration.getMappedStatement(statement);
    String sql = mappedStatement.getSqlSource().getSql();
    sql = sql.replaceAll("#\\{id}", "?");
    DataSource dataSource = configuration.getEnvironment().getDataSource();
    try {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, ((Object[]) params)[0].toString());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          List<T> objects = (List<T>) resultSetToObj(mappedStatement.getResultType(), resultSet);
          return CollectionUtil.isEmpty(objects) ? null : objects.get(0);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
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
          ReflectUtil.setFieldValue(o, field, rs.getObject(field.getName()));
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

  @Override
  public <T> T getMapper(Class<T> type) {
    return configuration.getMapper(type, this);
  }
}
