package cn.hutool.core.util.tests;

import cn.hutool.core.lang.ResourceClassLoader;
import cn.hutool.core.util.XmlUtil;
import com.exmaple.small.mybatis.datasource.DataSourceFactory;
import com.exmaple.small.mybatis.mapping.Environment;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.transaction.TransactionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Slf4j
public class XmlUtilTests {

  Configuration configuration = new Configuration();

  @Test
  public void test_readXml() throws IOException {
    try (InputStream inputStream =
        ResourceClassLoader.getSystemClassLoader().getResourceAsStream("UserMapper.xml")) {
      Document document = XmlUtil.readXML(inputStream);
      Element rootElement = XmlUtil.getRootElement(document);
      log.info(rootElement.getNodeName());
      log.info(rootElement.getAttribute("namespace"));

      XmlUtil.getElements(rootElement, "")
          .forEach(
              e -> {
                log.info("id: " + e.getAttribute("id"));
                log.info("parameterType: " + e.getAttribute("parameterType"));
                log.info("resultType: " + e.getAttribute("resultType"));
                log.info("sql: " + e.getTextContent());
              });
    }
  }

  @Test
  public void test_readMybatisConfigXml() {
    InputStream ins =
        ResourceClassLoader.getSystemClassLoader().getResourceAsStream("mybatis-config.xml");
    assert ins != null;
    Document document = XmlUtil.readXML(ins);
    Element rootElement = XmlUtil.getRootElement(document);
    // get environments element
    Element environmentsElement = XmlUtil.getElement(rootElement, "environments");
    // parse environment
    parseEnvironments(environmentsElement);

    // parse mappers
    Element mappersElement = XmlUtil.getElement(rootElement, "mappers");
    parseMappers(mappersElement);
  }

  // parse mappers
  private void parseMappers(Element mappersElement) {
    System.out.println(mappersElement.getNodeName());
    List<Element> mapperElements = XmlUtil.getElements(mappersElement, "");
    mapperElements.forEach(
        me -> {
          System.out.println("\t" + me.getAttribute("resource"));
        });
  }

  // parse environments node
  private void parseEnvironments(Element environmentsElement) {
    assert environmentsElement != null;
    // get environment element
    List<Element> environmentElements = XmlUtil.getElements(environmentsElement, "environment");
    environmentElements.forEach(
        eElement -> {
          System.out.println("environment: " + eElement.getAttribute("id"));
          TransactionFactory transactionFactory =
              parseTransactionManager(XmlUtil.getElement(eElement, "transactionManager"));
          DataSourceFactory dataSourceFactory =
              parseDataSource(XmlUtil.getElement(eElement, "dataSource"));

          Environment environment =
              Environment.builder()
                  .id(eElement.getAttribute("id"))
                  .transactionFactory(transactionFactory)
                  .dataSource(dataSourceFactory.getDataSource())
                  .build();

          configuration.setEnvironment(environment);
        });
  }

  // parse dataSource node
  private DataSourceFactory parseDataSource(Element dataSourceElement) {
    if (dataSourceElement == null) {
      throw new IllegalArgumentException("DataSource element cannot be null");
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
      throw new RuntimeException(e);
    }
  }

  // parse transactionManager node
  private TransactionFactory parseTransactionManager(Element transactionElement) {
    if (transactionElement == null) {
      throw new IllegalArgumentException("TransactionManager element is null");
    }
    String type = transactionElement.getAttribute("type");
    try {
      return (TransactionFactory)
          configuration.getTypeAliasRegistry().resolveAlias(type).newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException("TransactionFactory.class.newInstance error", e);
    }
  }
}
