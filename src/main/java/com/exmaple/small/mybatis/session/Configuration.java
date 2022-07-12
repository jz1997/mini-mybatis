package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.binding.MapperRegistry;
import com.exmaple.small.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.exmaple.small.mybatis.datasource.simple.SimpleDataSourceFactory;
import com.exmaple.small.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import com.exmaple.small.mybatis.executor.Executor;
import com.exmaple.small.mybatis.executor.ResultHandler;
import com.exmaple.small.mybatis.executor.SimpleExecutor;
import com.exmaple.small.mybatis.executor.statement.PrepareStatementHandler;
import com.exmaple.small.mybatis.executor.statement.SimpleStatementHandler;
import com.exmaple.small.mybatis.executor.statement.StatementHandler;
import com.exmaple.small.mybatis.mapping.Environment;
import com.exmaple.small.mybatis.transaction.JdbcTransactionFactory;
import com.exmaple.small.mybatis.transaction.Transaction;
import com.exmaple.small.mybatis.type.TypeAliasRegistry;
import com.exmaple.small.mybatis.type.TypeHandler;
import com.exmaple.small.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Configuration {

    protected MapperRegistry mapperRegistry = new MapperRegistry();
    protected Map<String, MappedStatement> mappedStatements = new HashMap<>();
    protected TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    protected TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    protected Environment environment;

    protected Set<String> alreadyLoadMapperSet = new HashSet<>();

    public Configuration() {
        // register transaction factory
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        // register datasource factory
        typeAliasRegistry.registerAlias("SIMPLE", SimpleDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnPooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public Configuration(Environment environment) {
        this();
        this.environment = environment;
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public StatementHandler newStatementHandler(
            MappedStatement ms, Object parameterObj, ResultHandler<?> resultHandler) {
        return new PrepareStatementHandler(ms, parameterObj, resultHandler);
    }

    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    public boolean isMapperAlreadyParsed(String resource) {
        return alreadyLoadMapperSet.contains(resource);
    }

    public void addParsedMapper(String resource) {
        alreadyLoadMapperSet.add(resource);
    }
}
