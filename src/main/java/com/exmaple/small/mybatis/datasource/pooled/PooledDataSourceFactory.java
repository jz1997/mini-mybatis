package com.exmaple.small.mybatis.datasource.pooled;

import com.exmaple.small.mybatis.datasource.unpooled.UnPooledDataSourceFactory;

/**
 *
 */
public class PooledDataSourceFactory extends UnPooledDataSourceFactory {
    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }
}
