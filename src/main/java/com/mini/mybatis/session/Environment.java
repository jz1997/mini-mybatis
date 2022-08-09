package com.mini.mybatis.session;

import com.mini.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

public class Environment {
    private String id;
    private TransactionFactory transactionFactory;
    private DataSource dataSource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
