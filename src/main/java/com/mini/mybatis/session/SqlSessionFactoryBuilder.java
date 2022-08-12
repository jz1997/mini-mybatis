package com.mini.mybatis.session;

import com.mini.mybatis.xml.XmlConfigBuilder;

import java.io.Reader;

public class SqlSessionFactoryBuilder {
    /**
     * 通过 xml 配置文件的额 reader 解析出 Configuration 进行构建
     *
     * @param reader 配置文件流
     * @return /
     */
    public SqlSessionFactory build(Reader reader) {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    /**
     * 直接通过配置文件进行构建
     *
     * @param configuration {@link  Configuration} 配置文件解析出的对象
     * @return /
     */
    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
