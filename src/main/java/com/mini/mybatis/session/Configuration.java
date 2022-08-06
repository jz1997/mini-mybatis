package com.mini.mybatis.session;

import com.mini.mybatis.datasource.factory.PooledDataSourceFactory;
import com.mini.mybatis.datasource.factory.SimpleDataSourceFactory;
import com.mini.mybatis.transaction.JdbcTransactionFactory;
import com.mini.mybatis.type.TypeAliasRegistry;
import com.mini.mybatis.type.TypeHandlerRegistry;
import com.mini.mybatis.xml.MapperStatement;

import java.util.HashSet;
import java.util.Set;

public class Configuration {
    private Environment environment;
    private Set<String> alreadyLoadedMapper = new HashSet<>();
    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    private TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    public Configuration() {
        // register transaction factory
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        // register datasource factory
        typeAliasRegistry.registerAlias("SIMPLE", SimpleDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public Configuration(Environment environment) {
        this();
        this.environment = environment;
    }

    public void addMapper(Class<?> mapperClass) {
        // todo: register mapper
    }

    public void addMapperStatement(MapperStatement ms) {
        // todo: register mapper statement
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public void setEnvironment(Environment e) {
        this.environment = e;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public boolean checkMapperResourceAlreadyLoaded(String resource) {
        return this.alreadyLoadedMapper.contains(resource);
    }

    public void loadMapperResource(String resource) {
        this.alreadyLoadedMapper.add(resource);
    }
}
