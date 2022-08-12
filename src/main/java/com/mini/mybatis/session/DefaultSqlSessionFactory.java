package com.mini.mybatis.session;

import com.mini.mybatis.transaction.Transaction;
import com.mini.mybatis.transaction.TransactionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Environment environment = configuration.getEnvironment();
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        Transaction transaction = transactionFactory.newTransaction(environment.getDataSource(), null, true);
        return new DefaultSqlSession(configuration, transaction);
    }
}
