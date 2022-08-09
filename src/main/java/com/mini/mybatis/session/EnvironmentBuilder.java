package com.mini.mybatis.session;

import com.mini.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

public final class EnvironmentBuilder {
    private String id;
    private TransactionFactory transactionFactory;
    private DataSource dataSource;

    public EnvironmentBuilder() {
    }

    public EnvironmentBuilder id(String id) {
        this.id = id;
        return this;
    }

    public EnvironmentBuilder transactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
        return this;
    }

    public EnvironmentBuilder dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public Environment build() {
        Environment environment = new Environment();
        environment.setId(id);
        environment.setTransactionFactory(transactionFactory);
        environment.setDataSource(dataSource);
        return environment;
    }
}
