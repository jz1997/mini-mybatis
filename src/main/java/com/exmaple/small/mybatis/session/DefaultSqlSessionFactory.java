package com.exmaple.small.mybatis.session;

import com.exmaple.small.mybatis.executor.Executor;
import com.exmaple.small.mybatis.mapping.Environment;
import com.exmaple.small.mybatis.transaction.Transaction;

/**
 * 默认的 SqlSession 工厂类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        final Environment environment = configuration.getEnvironment();
        Transaction transaction =
                environment.getTransactionFactory().newTransaction(environment.getDataSource(), null, true);
        Executor executor = configuration.newExecutor(transaction);
        return new DefaultSqlSession(configuration, executor);
    }
}
