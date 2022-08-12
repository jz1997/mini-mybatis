package com.mini.mybatis.session;

import com.mini.mybatis.xml.XmlConfigBuilder;

import java.io.Reader;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(Reader reader){
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }
    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
