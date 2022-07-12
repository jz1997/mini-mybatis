package com.exmaple.small.mybatis.datasource.unpooled;

import cn.hutool.core.util.ReflectUtil;
import com.exmaple.small.mybatis.datasource.DataSourceFactory;

import java.util.*;
import javax.sql.DataSource;

/**
 * 无池化数据源构建工厂
 */
public class UnPooledDataSourceFactory implements DataSourceFactory {

    private static final String driverPropertyPrefix = "driver.";

    protected DataSource dataSource;

    public UnPooledDataSourceFactory() {
        this.dataSource = new UnPooledDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        final Properties driverProperties = new Properties();
        properties.forEach(
                (nameObj, value) -> {
                    String name = (String) nameObj;
                    boolean hasField = ReflectUtil.hasField(UnPooledDataSource.class, name);
                    if (name.startsWith(driverPropertyPrefix)) {
                        String propertyName = name.replaceFirst(driverPropertyPrefix, "");
                        driverProperties.put(propertyName, value);
                    } else if (hasField) {
                        ReflectUtil.setFieldValue(dataSource, name, value);
                    } else {
                        throw new IllegalArgumentException("Unknown dataSource property: " + name);
                    }
                });

        // extra properties
        if (!driverProperties.isEmpty()) {
            ReflectUtil.setFieldValue(dataSource, "driverProperties", driverProperties);
        }
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
