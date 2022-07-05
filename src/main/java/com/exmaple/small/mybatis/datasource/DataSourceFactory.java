package com.exmaple.small.mybatis.datasource;

import java.util.*;
import javax.sql.DataSource;

/** */
public interface DataSourceFactory {

  void setProperties(Properties properties);

  DataSource getDataSource();
}
