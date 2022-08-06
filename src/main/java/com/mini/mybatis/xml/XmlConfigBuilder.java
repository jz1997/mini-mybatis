package com.mini.mybatis.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.db.DbUtil;
import com.mini.mybatis.builder.BaseBuilder;
import com.mini.mybatis.datasource.factory.DataSourceFactory;
import com.mini.mybatis.exception.XmlParseException;
import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.session.Environment;
import com.mini.mybatis.session.EnvironmentBuilder;
import com.mini.mybatis.transaction.TransactionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.sql.DataSource;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class XmlConfigBuilder extends BaseBuilder {

    private final Element root;
    private String environment;

    public XmlConfigBuilder(Reader reader) {
        super(new Configuration());
        try {
            Document document = XmlUtil.readXML(reader);
            root = XmlUtil.getRootElement(document);
        } finally {
            DbUtil.close(reader);
        }
    }

    public void parse() {
        parseEnvironmentsElement(XmlUtil.getElement(root, "environments"));
        parseMappersElement(XmlUtil.getElement(root, "mappers"));
    }

    private void parseEnvironmentsElement(Element environmentsElement) {
        if (environmentsElement == null) {
            throw new XmlParseException("Environments 节点不能为空");
        }

        // get default attribute value
        environment = environmentsElement.getAttribute("default");

        List<Element> environmentElements = XmlUtil.getElements(environmentsElement, "environment");
        checkHasDefaultEnvironment(environmentElements);

        for (Element environmentElement : environmentElements) {
            String environmentId = environmentElement.getAttribute("id");
            if (Objects.equals(environmentId, environment)) {
                Environment e = parseEnvironmentElement(environmentElement);
                configuration.setEnvironment(e);
            }
        }
    }


    private void checkHasDefaultEnvironment(List<Element> environmentElements) {
        boolean isValidDefaultEnvironment = environmentElements.stream()
                .map(e -> e.getAttribute("id"))
                .anyMatch(id -> Objects.equals(id, this.environment));
        if (!isValidDefaultEnvironment) {
            throw new XmlParseException("无效的默认环境: " + this.environment);
        }
    }

    private Environment parseEnvironmentElement(Element environmentElement) {
        Element transactionManagerElement = XmlUtil.getElement(environmentElement, "transactionManager");
        TransactionFactory transactionFactory = parseTransactionManagerElement(transactionManagerElement);

        Element dataSourceElement = XmlUtil.getElement(environmentElement, "dataSource");
        DataSource dataSource = parseDataSourceElement(dataSourceElement);

        return new EnvironmentBuilder()
                .id(environmentElement.getAttribute("id"))
                .transactionFactory(transactionFactory)
                .dataSource(dataSource)
                .build();

    }

    private TransactionFactory parseTransactionManagerElement(Element element) {
        if (element == null) {
            throw new XmlParseException("transactionManager 节点不能缺省");
        }
        String type = element.getAttribute("type");
        try {
            return (TransactionFactory) typeAliasRegistry.resolveAlias(type).getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new XmlParseException("创建事务工厂对象失败, 事务类型: " + type + ", 失败原因: " + e.getMessage(), e);
        }
    }

    private DataSource parseDataSourceElement(Element element) {
        if (element == null) {
            throw new XmlParseException("dataSource 节点不能缺省");
        }

        String type = element.getAttribute("type");

        try {
            DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(type).getConstructor().newInstance();
            Properties properties = childElementsToProperties(element);
            dataSourceFactory.setProperties(properties);
            return dataSourceFactory.getDataSource();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new XmlParseException("创建数据源工厂失败, 数据源类型: " + type + ", 失败原因: " + e.getMessage(), e);
        }
    }


    /**
     * Mappers 节点的解析
     */
    private void parseMappersElement(Element mappersElement) {
        if (mappersElement == null) {
            return;
        }

        List<Element> mapperElements = XmlUtil.getElements(mappersElement, "mapper");
        for (Element mapperElement : mapperElements) {
            String resource = mapperElement.getAttribute("resource");
            if (StrUtil.isBlank(resource)) {
                throw new XmlParseException("Mapper 节点中的 resource 属性不能缺省");
            }
            XmlMapperBuilder mapperBuilder = new XmlMapperBuilder(configuration, resource);
            mapperBuilder.parse();
        }
    }


    // XML 子节点列表中的 name 和 value 标签封装为 Properties 对象
    private static Properties childElementsToProperties(Element element) {
        List<Element> propertyElements = XmlUtil.getElements(element, null);
        final Properties properties = new Properties();
        propertyElements.forEach(e -> {
            String name = e.getAttribute("name");
            String value = e.getAttribute("value");
            properties.put(name, value);
        });
        return properties;
    }
}
