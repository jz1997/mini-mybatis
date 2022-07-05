package com.exmaple.small.mybatis.mapping;

import com.exmaple.small.mybatis.transaction.TransactionFactory;
import javax.sql.DataSource;

public class Environment {

  private String id;

  private TransactionFactory transactionFactory;

  private DataSource dataSource;

  public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
    if (id == null) {
      throw new IllegalArgumentException("Environment id cannot be null");
    }

    if (transactionFactory == null) {
      throw new IllegalArgumentException("Environment transactionFactory cannot be null");
    }

    if (dataSource == null) {
      throw new IllegalArgumentException("Environment dataSource cannot be null");
    }

    this.id = id;
    this.transactionFactory = transactionFactory;
    this.dataSource = dataSource;
  }

  public String getId() {
    return id;
  }

  public TransactionFactory getTransactionFactory() {
    return transactionFactory;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  // Environment Builder

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String id;
    private TransactionFactory transactionFactory;
    private DataSource dataSource;

    private Builder() {}

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder transactionFactory(TransactionFactory transactionFactory) {
      this.transactionFactory = transactionFactory;
      return this;
    }

    public Builder dataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
    }

    public Environment build() {
      return new Environment(id, transactionFactory, dataSource);
    }
  }
}
