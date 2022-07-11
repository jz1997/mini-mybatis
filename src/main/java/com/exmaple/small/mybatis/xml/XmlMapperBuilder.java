package com.exmaple.small.mybatis.xml;

import cn.hutool.core.lang.ResourceClassLoader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.exmaple.small.mybatis.builder.BasicBuilder;
import com.exmaple.small.mybatis.session.Configuration;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Slf4j
public class XmlMapperBuilder extends BasicBuilder {

  private String namespace;
  private String resource;

  public XmlMapperBuilder(Configuration configuration) {
    super(configuration);
  }

  public XmlMapperBuilder(Configuration configuration, String resource) {
    this(configuration);
    this.resource = resource;
  }

  public void parse() {
    // check this mapper file is already parsed
    if (configuration.isMapperAlreadyParsed(resource)) {
      return;
    }
    // record this mapper file is parsed
    configuration.addParsedMapper(resource);

    // parse mapper file
    Element root = readerMapperFile();
    parseMapperFileAttributes(root);
    // parse each sql element
    XmlUtil.getElements(root, null).forEach(this::parseStatement);
  }

  private void parseStatement(Element sqlElement) {
    XmlStatementBuilder statementBuilder =
        new XmlStatementBuilder(configuration, namespace, sqlElement);
    statementBuilder.parse();
  }

  private void parseMapperFileAttributes(Element root) {
    namespace = root.getAttribute("namespace");
    if (StrUtil.isBlank(namespace)) {
      log.error("The 'namespace' attribute must be set in the {} file.", resource);
      throw new RuntimeException(
          "The 'namespace' attribute must be set in the " + resource + " file.");
    }

    try {
      configuration.addMapper(Class.forName(namespace));
    } catch (ClassNotFoundException e) {
      log.error("Class.forName error. Cause: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private Element readerMapperFile() {
    try (InputStream ins = ResourceClassLoader.getSystemResourceAsStream(resource)) {
      Document document = XmlUtil.readXML(ins);
      return XmlUtil.getRootElement(document);
    } catch (Exception e) {
      log.error("Read mapper file error. Cause: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
