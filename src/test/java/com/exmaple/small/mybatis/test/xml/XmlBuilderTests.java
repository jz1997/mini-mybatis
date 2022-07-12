package com.exmaple.small.mybatis.test.xml;

import cn.hutool.core.lang.ResourceClassLoader;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.xml.XmlConfigBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class XmlBuilderTests {

    @Test
    public void test_parse() {
        InputStream inputStream = ResourceClassLoader.getSystemResourceAsStream("mybatis-config.xml");
        assert inputStream != null;
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(new InputStreamReader(inputStream));
        Configuration configuration = xmlConfigBuilder.parse();
        log.info("Parse configuration: {}", configuration);
        Assertions.assertNotNull(configuration);
    }
}
