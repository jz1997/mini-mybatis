package com.exmaple.small.mybatis.test.xml;

import cn.hutool.core.lang.ResourceClassLoader;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.xml.XmlConfigBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Test;

public class XmlBuilderTests {

  @Test
  public void test_parse() {
    InputStream inputStream = ResourceClassLoader.getSystemResourceAsStream("mybatis-config.xml");
    assert inputStream != null;
    XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(new InputStreamReader(inputStream));
    Configuration configuration = xmlConfigBuilder.parse();
    System.out.println(configuration);
  }
}
