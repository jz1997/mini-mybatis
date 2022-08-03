package com.mini.mybatis.datasource.factory;

import com.mini.mybatis.datasource.PooledDataSource;

public class PooledDataSourceFactory extends SimpleDataSourceFactory {
    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }
}
