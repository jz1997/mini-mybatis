package com.exmaple.small.mybatis.executor.parameter;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.parsing.ParameterMapping;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.session.Configuration;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultParameterHandler implements ParameterHandler {

  private final MappedStatement mappedStatement;
  private final Configuration configuration;
  private final Object parameterObject;

  public DefaultParameterHandler(
      MappedStatement mappedStatement, Configuration configuration, Object parameterObject) {
    Assert.notNull(mappedStatement, "MappedStatement cannot be null");
    Assert.notNull(configuration, "Configuration cannot be null");
    Assert.notNull(parameterObject, "ParameterObject cannot be null");
    this.mappedStatement = mappedStatement;
    this.configuration = configuration;
    this.parameterObject = parameterObject;
  }

  @Override
  public Object getParameterObject() {
    return this.parameterObject;
  }

  @Override
  public void setParameters(PreparedStatement ps) throws SQLException {
    BoundSql boundSql = this.mappedStatement.getSqlSource().getBoundSql(parameterObject);
    Object[] params = (Object[]) this.parameterObject;
    Object firstParameter = params.length > 0 ? params[0] : null;
    if (firstParameter instanceof Map parameterMap) {
      for (int i = 0; i < boundSql.getParameterMappings().size(); i++) {
        ParameterMapping parameterMapping = boundSql.getParameterMappings().get(i);
        String property = parameterMapping.getProperty();
        Object propertyValue = parameterMap.get(property);
        ps.setObject(i + 1, propertyValue);
      }
    }  else {
        for (int i = 0; i < boundSql.getParameterMappings().size(); i++) {
          String property = boundSql.getParameterMappings().get(i).getProperty();
          try {
            Object value = ReflectUtil.getFieldValue(firstParameter, property);
            ps.setObject(i + 1, value);
          } catch (Exception e) {
            throw new RuntimeException("Can not get property value of " + property + ". Cause: " + e, e);
          }
        }
    }
  }
}
