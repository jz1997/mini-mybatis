package com.exmaple.small.mybatis.xml;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.builder.BasicBuilder;
import com.exmaple.small.mybatis.builder.SqlSourceBuilder;
import com.exmaple.small.mybatis.datasource.DataSourceFactory;
import com.exmaple.small.mybatis.mapping.Environment;
import com.exmaple.small.mybatis.session.SqlSource;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.transaction.TransactionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** */
@Slf4j
public class XmlConfigBuilder extends BasicBuilder {
  /** */
  private Element root;

  private String environment;

  public XmlConfigBuilder(Reader reader) {
    super(new Configuration());
    try {
      Document document = XmlUtil.readXML(reader);
      root = XmlUtil.getRootElement(document);
    } catch (Exception e) {
      log.error("read xml error", e);
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        log.error("Close mapper xml file reader error", e);
      }
    }
  }

  public Configuration parse() {
    // parse environments
    parseEnvironmentsElement(XmlUtil.getElement(root, "environments"));
    // parse mappers
    parseMappersElement(XmlUtil.getElement(root, "mappers"));
    return configuration;
  }

  // parse environments element
  private void parseEnvironmentsElement(Element environmentsElement) {
    assert environmentsElement != null;
    // get default environment id
    environment = environmentsElement.getAttribute("default");

    // get environment element
    List<Element> environmentElements = XmlUtil.getElements(environmentsElement, "environment");
    for (Element environmentElement : environmentElements) {
      String environmentId = environmentElement.getAttribute("id");
      if (isDefaultEnvironment(environmentId)) {
        Environment environment = parseEnvironmentElement(environmentElement);
        configuration.setEnvironment(environment);
      }
    }
  }

  // parse environment element
  private Environment parseEnvironmentElement(Element environmentElement) {
    // parse transactionManager element
    TransactionFactory transactionFactory =
        parseTransactionManagerElement(
            XmlUtil.getElement(environmentElement, "transactionManager"));
    // parse dataSource element
    DataSourceFactory dataSourceFactory =
        parseDataSourceElement(XmlUtil.getElement(environmentElement, "dataSource"));

    return Environment.builder()
        .id(environmentElement.getAttribute("id"))
        .dataSource(dataSourceFactory.getDataSource())
        .transactionFactory(transactionFactory)
        .build();
  }

  // check element is default element
  private boolean isDefaultEnvironment(String checkEnvironmentId) {
    if (StrUtil.isBlank(this.environment)) {
      throw new IllegalArgumentException(
          "The property 'default' of the 'Environments' element is required.");
    }
    return Objects.equals(this.environment, checkEnvironmentId);
  }

  // parse transactionManager element
  private TransactionFactory parseTransactionManagerElement(Element transactionElement) {
    if (transactionElement == null) {
      throw new IllegalArgumentException("TransactionManager element is required.");
    }
    String type = transactionElement.getAttribute("type");
    try {
      return (TransactionFactory)
          configuration.getTypeAliasRegistry().resolveAlias(type).newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException("TransactionFactory.class.newInstance error", e);
    }
  }

  // parse dataSource node
  private DataSourceFactory parseDataSourceElement(Element dataSourceElement) {
    if (dataSourceElement == null) {
      throw new IllegalArgumentException("DataSource element is required.");
    }

    String type = dataSourceElement.getAttribute("type");
    try {
      DataSourceFactory dataSourceFactory =
          (DataSourceFactory) configuration.getTypeAliasRegistry().resolveAlias(type).newInstance();
      final Properties properties = new Properties();
      XmlUtil.getElements(dataSourceElement, "")
          .forEach(
              e -> {
                properties.put(e.getAttribute("name"), e.getAttribute("value"));
              });
      dataSourceFactory.setProperties(properties);
      return dataSourceFactory;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException("DataSourceFactory.class.newInstance error", e);
    }
  }

  // parse mappers element
  private void parseMappersElement(Element mappersElement) {
    List<Element> elements = XmlUtil.getElements(mappersElement, null);
    elements.forEach(this::parseMapperElement);
  }

  private void parseMapperElement(Element mapperElement) {
    String mapperResource = mapperElement.getAttribute("resource");
    try (InputStream ins = ClassLoaderUtil.getClassLoader().getResourceAsStream(mapperResource)) {
      Document document = XmlUtil.readXML(ins);
      parseMapperXml(XmlUtil.getRootElement(document));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void parseMapperXml(Element root) {
    final String namespace = root.getAttribute("namespace");
    List<Element> msElements = XmlUtil.getElements(root, "");
    msElements.stream()
        .map(e -> this.elementToMappedStatement(namespace, e))
        .forEach(mappedStatement -> configuration.addMappedStatement(mappedStatement));

    // Add Mapper
    try {
      configuration.addMapper(Class.forName(namespace));
    } catch (ClassNotFoundException ex) {
      log.info("Mapper class not found: {}, err: {}", namespace, ex.getMessage());
      throw new RuntimeException(ex);
    }
  }

  private MappedStatement elementToMappedStatement(String namespace, Element e) {
    String id = e.getAttribute("id");
    String parameterType = e.getAttribute("parameterType");
    String resultType = e.getAttribute("resultType");
    String sql = e.getTextContent();
    String msId = namespace + "." + id;
    SqlSource sqlSource;
    try {
      sqlSource = new SqlSourceBuilder(configuration).parse(sql, Class.forName(parameterType));
    } catch (ClassNotFoundException ex) {
      log.error("Class not found: {}.", parameterType);
      throw new RuntimeException(ex);
    }
    return new MappedStatement(
        configuration, msId, namespace, sqlSource, parameterType, resultType, e.getNodeName());
  }
}
