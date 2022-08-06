package com.mini.mybatis.xml;

import cn.hutool.core.util.ClassLoaderUtil;
import com.mini.mybatis.datasource.SimpleDataSource;
import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.session.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class XmlConfigBuilderTest {

    @Test
    public void test_parse_jdbc_simple() {
        InputStream ins = ClassLoaderUtil.getClassLoader().getResourceAsStream("mybatis-config-jdbc-simple.xml");
        Assertions.assertNotNull(ins);
        Reader reader = new InputStreamReader(ins);

        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        xmlConfigBuilder.parse();
        Configuration configuration = xmlConfigBuilder.getConfiguration();

        Environment environment = configuration.getEnvironment();
        Assertions.assertNotNull(environment);
        Assertions.assertNotNull(environment.getDataSource());
        Assertions.assertNotNull(environment.getTransactionFactory());

        SimpleDataSource dataSource = (SimpleDataSource) environment.getDataSource();
        Assertions.assertEquals(dataSource.getDriver(), "com.mysql.cj.jdbc.Driver");
        Assertions.assertEquals(dataSource.getUsername(), "root");
        Assertions.assertEquals(dataSource.getPassword(), "123456");
        Properties driverProperties = dataSource.getDriverProperties();
        Assertions.assertNotNull(driverProperties);
        Assertions.assertEquals(driverProperties.getProperty("first"), "first");
        Assertions.assertEquals(driverProperties.getProperty("second"), "second");
        Assertions.assertEquals(driverProperties.getProperty("third"), "third");

    }
}
