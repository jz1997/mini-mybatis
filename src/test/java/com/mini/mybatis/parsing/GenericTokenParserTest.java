package com.mini.mybatis.parsing;

import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.session.SqlSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class GenericTokenParserTest {

    @Test
    public void test_parse() {
        GenericTokenParser genericTokenParser = new GenericTokenParser("#\\{", "}", new SqlSourceBuilder.ParameterMappingHandler(new Configuration(), String.class));
        String originalSql = "select * from student where name = #{name} and age = #{age}";
        String exceptedSql = "select * from student where name = ? and age = ?";
        String parsedSql = genericTokenParser.parse(originalSql);
        log.info("Original Sql: {}", originalSql);
        log.info("Parsed Sql: {}", parsedSql);
        Assertions.assertEquals(exceptedSql, parsedSql);
    }
}