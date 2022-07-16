package com.exmaple.small.mybatis.xml;

import cn.hutool.core.lang.Assert;
import com.exmaple.small.mybatis.binding.SqlCommandType;
import lombok.Data;
import org.w3c.dom.Element;

import java.util.Locale;

@Data
public class XmlStatement {
    private String namespace;
    private String id;
    private String parameterType;
    private String resultType;
    private String originalSql;
    private SqlCommandType sqlCommandType;

    public XmlStatement() {
    }

    public XmlStatement(String namespace, Element element) {
        String id = element.getAttribute("id");
        String parameterType = element.getAttribute("parameterType");
        String resultType = element.getAttribute("resultType");
        String originalSql = element.getTextContent();
        String sqlCommandType = element.getTagName();

        // check parameters
        Assert.notNull(namespace);
        Assert.notNull(id);
        Assert.notNull(parameterType);
        Assert.notNull(resultType);
        Assert.notNull(originalSql);
        Assert.notNull(sqlCommandType);

        this.namespace = namespace;
        this.id = id;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.originalSql = originalSql;
        this.sqlCommandType = SqlCommandType.valueOf(sqlCommandType.toUpperCase(Locale.ENGLISH));
    }
}
