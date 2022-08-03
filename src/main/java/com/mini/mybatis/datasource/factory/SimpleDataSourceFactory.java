package com.mini.mybatis.datasource.factory;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.mini.mybatis.datasource.SimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Properties;

public class SimpleDataSourceFactory implements DataSourceFactory {
    /**
     * 对 DataSource 的配置 键名前缀
     */
    protected static final String DRIVER_PROPERTY_PREFIX = "driver.";

    protected DataSource dataSource;

    public SimpleDataSourceFactory() {
        dataSource = new SimpleDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        Properties driverProperties = new Properties();
        for (Object oKey : properties.keySet()) {
            String key = oKey.toString();
            Object value = properties.getProperty(key);
            if (key.startsWith(DRIVER_PROPERTY_PREFIX)) {
                String driverKey = key.substring(DRIVER_PROPERTY_PREFIX.length());
                driverProperties.put(driverKey, value);
            } else {
                Field field = ReflectUtil.getField(dataSource.getClass(), key);
                Assert.notNull(field, "DataSource 属性不存在, key: " + key);
                ReflectUtil.setFieldValue(dataSource, field, value);
            }
        }


        // 设置数据源配置
        if (!driverProperties.isEmpty()) {
            ReflectUtil.setFieldValue(dataSource, "driverProperties", driverProperties);
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
