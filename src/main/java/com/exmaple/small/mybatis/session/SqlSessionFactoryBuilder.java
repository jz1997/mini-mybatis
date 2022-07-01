package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.xml.XmlConfigBuilder;
import java.io.Reader;
import java.util.*;

/** */
public class SqlSessionFactoryBuilder {

  public SqlSessionFactoryBuilder() {}

  public SqlSessionFactory build(Reader reader) {
    XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
    return this.build(xmlConfigBuilder.parse());
  }

  public SqlSessionFactory build(Configuration configuration) {
    return new DefaultSqlSessionFactory(configuration);
  }
}
