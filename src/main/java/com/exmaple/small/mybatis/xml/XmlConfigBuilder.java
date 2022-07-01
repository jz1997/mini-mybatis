package com.exmaple.small.mybatis.xml;

import cn.hutool.core.util.XmlUtil;
import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.builder.BasicBuilder;
import com.exmaple.small.mybatis.mapping.SqlSource;
import com.exmaple.small.mybatis.session.Configuration;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** */
@Slf4j
public class XmlConfigBuilder extends BasicBuilder {
  /** */
  private Document document;

  public XmlConfigBuilder(Reader reader) {
    super(new Configuration());
    // get document from reader
    try {
      document = XmlUtil.readXML(reader);
    } catch (Exception e) {
      log.error("read xml error", e);
    }
  }

  public Configuration parse() {
    // get root element
    Element rootElement = XmlUtil.getRootElement(document);
    mapperElement(rootElement);
    return configuration;
  }

  private void mapperElement(Element rootElement) {
    final String namespace = rootElement.getAttribute("namespace");
    List<Element> msElements = XmlUtil.getElements(rootElement, "");
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
    SqlSource sqlSource = new SqlSource(sql);
    String msId = namespace + "." + id;
    return new MappedStatement(
        msId, namespace, sqlSource, parameterType, resultType, e.getNodeName());
  }
}
