package com.exmaple.small.mybatis.test.parsing;

import cn.hutool.core.lang.ResourceClassLoader;
import com.exmaple.small.mybatis.builder.SqlSourceBuilder.ParameterMappingHandler;
import com.exmaple.small.mybatis.parsing.GenericTokenParser;
import com.exmaple.small.mybatis.parsing.TokenHandler;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.xml.XmlConfigBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class GenericTokenParserTests {
  @Test
  public void test_parse_one_token_success() {
    InputStream inputStream =
        ResourceClassLoader.getSystemResourceAsStream("pooled/mybatis-config-pooled.xml");
    assert inputStream != null;
    Configuration configuration = new XmlConfigBuilder(new InputStreamReader(inputStream)).parse();
    TokenHandler tokenHandler = new ParameterMappingHandler(configuration, Map.class);
    GenericTokenParser genericTokenParser = new GenericTokenParser("#\\{", "\\}", tokenHandler);
    String sql = genericTokenParser.parse("select *  from user where id = #{id}");
    Assertions.assertNotNull(sql);
    Assertions.assertNotEquals(sql, "");
    log.info("Parsed original sql: {}", sql);
  }
}
