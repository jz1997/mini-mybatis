package com.exmaple.small.mybatis.datasource.unpooled;

import com.exmaple.small.mybatis.datasource.AbstractDataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class UnPooledDataSource extends AbstractDataSource {

  protected Properties driverProperties;

  /** */
  protected String driver;

  /** */
  protected String url;

  /** */
  protected String username;

  /** */
  protected String password;

  /** */
  protected Integer level;

  /** */
  protected Boolean autocommit;

  /** */
  protected static final Map<String, Driver> registerDrivers = new ConcurrentHashMap<>();

  static {
    // 加载已经注册的驱动
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
      Driver registerDriver = drivers.nextElement();
      registerDrivers.put(registerDriver.getClass().getName(), registerDriver);
    }
  }

  public UnPooledDataSource() {}

  @Override
  public Connection getConnection() throws SQLException {
    return doGetConnection(this.username, this.password);
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return doGetConnection(username, password);
  }

  private Connection doGetConnection(String username, String password) throws SQLException {
    Properties properties = new Properties();
    if (this.driverProperties != null) {
      properties.putAll(this.driverProperties);
    }

    if (username != null) {
      properties.put("user", username);
    }

    if (password != null) {
      properties.put("password", password);
    }

    initializeDriver();
    Connection connection = DriverManager.getConnection(url, properties);
    configurationConnection(connection);

    if (log.isDebugEnabled()) {
      log.debug("Get connection: {} from UnpooledDataSource.", connection);
    }
    return connection;
  }

  private void configurationConnection(Connection connection) throws SQLException {
    if (this.level != null) {
      connection.setTransactionIsolation(level);
    }

    if (this.autocommit != null) {
      connection.setAutoCommit(autocommit);
    }
  }

  // 注册数据库驱动
  private synchronized void initializeDriver() throws SQLException {
    if (!registerDrivers.containsKey(driver)) {
      try {
        Driver loadDriver = (Driver) Class.forName(driver).newInstance();
        DriverManager.registerDriver(loadDriver);
        registerDrivers.put(driver, loadDriver);
      } catch (Exception e) {
        throw new SQLException("Can not setting sql driver on UnPooledDataSource. Cause: " + e);
      }
    }
  }
}
