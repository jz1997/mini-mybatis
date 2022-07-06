package com.exmaple.small.mybatis.builder;

import com.exmaple.small.mybatis.session.Configuration;
import java.io.Reader;

/** 通用 Builder */
public abstract class BasicBuilder {

  /** Configuration */
  protected Configuration configuration;

  /** Default constructor */
  public BasicBuilder(Configuration configuration) {
    this.configuration = configuration;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
