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
    // 数据源环境配置
    private Environment environment;
    // 已经记载的 mapper 资源文件
    private Set<String> alreadyLoadedMapper = new HashSet<>();
    // 类型注册器
    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    // 类型转换注册器
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

    /**
     * 注册 Mapper 接口文件
     *
     * @param mapperClass 例如 UserMapper.class
     */
    public void addMapper(Class<?> mapperClass) {
        // todo: register mapper
    }

    /**
     * 注册 mapper.xml 中解析的 SQL 节点信息
     * @param ms /
     */
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

    /**
     * 检查资源文件是否已经注册
     *
     * @param resource 需检查的资源文件
     * @return /
     */
    public boolean checkMapperResourceAlreadyLoaded(String resource) {
        return this.alreadyLoadedMapper.contains(resource);
    }


    /**
     * 注册 mapper 资源文件
     *
     * @param resource 资源文件地址
     */
    public void registryMapperResource(String resource) {
        this.alreadyLoadedMapper.add(resource);
    }
}
