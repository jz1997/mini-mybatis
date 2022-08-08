package com.mini.mybatis.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.mini.mybatis.builder.BaseBuilder;
import com.mini.mybatis.exception.XmlParseException;
import com.mini.mybatis.session.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class XmlMapperBuilder extends BaseBuilder {
    // mapper.xml 文件地址
    private final String resource;

    // mapper.xml 文件的总结点
    private final Element root;

    // mapper.xml 中 mapper 节点中的 namespace 属性
    private String namespace;

    // sql 操作节点名称
    private final String[] commandTagNames = {"select", "insert", "update", "delete"};

    public XmlMapperBuilder(Configuration configuration, String resource) {
        super(configuration);
        try {
            this.resource = resource;
            Document document = XmlUtil.readXML(resource);
            this.root = XmlUtil.getRootElement(document);
        } catch (Exception e) {
            throw new XmlParseException("读取 Mapper 文件失败, Mapper: " + resource + ", 失败原因: " + e.getMessage(), e);
        }
    }

    public void parse() {
        boolean loaded = configuration.checkMapperResourceAlreadyLoaded(this.resource);
        if (loaded) {
            return;
        }

        configuration.loadMapperResource(this.resource);

        this.namespace = root.getAttribute("namespace");
        if (StrUtil.isBlank(this.namespace)) {
            throw new XmlParseException("namespace 属性不能为空");
        }

        // register mapper class
        registerMapper();

        parseSqlCommandElement();
    }

    private void registerMapper() {
        try {
            Class<?> mapperClass = Class.forName(this.namespace);
            configuration.addMapper(mapperClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 解析 sql 命令节点, select, update, delete, update
    private void parseSqlCommandElement() {
        for (String commandTagName : commandTagNames) {
            List<Element> elements = XmlUtil.getElements(this.root, commandTagName);
            for (Element element : elements) {
                XmlStatementBuilder statementBuilder = new XmlStatementBuilder(configuration, this.namespace, element);
                statementBuilder.parse();
            }
        }
    }
}
