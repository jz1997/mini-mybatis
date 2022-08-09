package com.mini.mybatis.builder;

import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.type.TypeAliasRegistry;
import com.mini.mybatis.type.TypeHandlerRegistry;

public class BaseBuilder {
    protected Configuration configuration;
    protected TypeAliasRegistry typeAliasRegistry;
    protected TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }
}
