package com.exmaple.small.mybatis.builder;

import com.exmaple.small.mybatis.parsing.GenericTokenParser;
import com.exmaple.small.mybatis.parsing.ParameterMapping;
import com.exmaple.small.mybatis.parsing.TokenHandler;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSource;
import com.exmaple.small.mybatis.session.StaticSqlSource;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SqlSourceBuilder extends BasicBuilder {

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType) {
        ParameterMappingHandler tokenHandler =
                new ParameterMappingHandler(configuration, parameterType);
        GenericTokenParser parser = new GenericTokenParser("#\\{", "\\}", tokenHandler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(sql, configuration, tokenHandler.getParameterMappings());
    }

    @Getter
    public static class ParameterMappingHandler extends BasicBuilder implements TokenHandler {
        private final List<ParameterMapping> parameterMappings = new ArrayList<>();
        private final Class<?> parameterType;

        public ParameterMappingHandler(Configuration configuration, Class<?> parameterType) {
            super(configuration);
            this.parameterType = parameterType;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private ParameterMapping buildParameterMapping(String property) {
            return ParameterMapping.builder().property(property).build();
        }
    }
}
