package com.exmaple.small.mybatis.xml;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.session.Configuration;
import org.w3c.dom.Element;

public class XmlStatementBuilder {

    private Configuration configuration;

    private String namespace;

    private Element element;

    public XmlStatementBuilder(Configuration configuration, String namespace, Element element) {
        this.configuration = configuration;
        this.element = element;
        this.namespace = namespace;
    }

    public void parse() {
        XmlStatement xmlStatement = new XmlStatement(namespace, element);
        MappedStatement ms = new MappedStatement(configuration, xmlStatement);
        configuration.addMappedStatement(ms);
    }
}
