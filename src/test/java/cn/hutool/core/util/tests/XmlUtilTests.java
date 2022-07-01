package cn.hutool.core.util.tests;

import cn.hutool.core.lang.ResourceClassLoader;
import cn.hutool.core.util.XmlUtil;
import com.sun.xml.internal.bind.v2.model.core.ID;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Slf4j
public class XmlUtilTests {
  @Test
  public void test_readXml() throws IOException {
    try (InputStream inputStream =
        ResourceClassLoader.getSystemClassLoader().getResourceAsStream("userMapper.xml")) {
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
}
