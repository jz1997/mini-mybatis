package com.mini.mybatis.session;

import com.mini.mybatis.builder.BaseBuilder;
import com.mini.mybatis.parsing.GenericTokenParser;
import com.mini.mybatis.parsing.ParameterMapping;
import com.mini.mybatis.parsing.TokenHandler;
import com.mini.mybatis.type.TypeHandlerRegistry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SqlSourceBuilder extends BaseBuilder {
    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource build(String originalSql, Class<?> parameterType) {
        ParameterMappingHandler parameterMappingHandler = new ParameterMappingHandler(configuration, parameterType);
        GenericTokenParser parser = new GenericTokenParser("#\\{", "}", parameterMappingHandler);
        String parsedSql = parser.parse(originalSql);
        return new StaticSqlSource(parsedSql, parameterMappingHandler.getParameterMappings(), configuration);
    }

    @Getter
    public static class ParameterMappingHandler extends BaseBuilder implements TokenHandler {
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
            return ParameterMapping.builder()
                    .property(property)
                    .build();
        }
    }

}
