package com.mini.mybatis.xml;

import cn.hutool.core.util.StrUtil;
import com.mini.mybatis.builder.BaseBuilder;
import com.mini.mybatis.exception.XmlParseException;
import com.mini.mybatis.session.Configuration;
import org.w3c.dom.Element;

public class XmlStatementBuilder extends BaseBuilder {

    // com.mini.mybatis.mapper.UserMapper
    private final String namespace;

    // sql 命令节点 insert, update, select, delete
    private final Element element;

    public XmlStatementBuilder(Configuration configuration, String namespace, Element element) {
        super(configuration);
        this.namespace = namespace;
        this.element = element;
    }

    public void parse() {
        String id = element.getAttribute("id");
        String parameterType = element.getAttribute("parameterType");
        String resultType = element.getAttribute("resultType");

        // todo: parse sql source
        String sql = element.getTextContent();

        if (StrUtil.isBlank(id)) {
            throw new XmlParseException("sql 操作节点中的 id 属性不能为空, Mapper: " + this.namespace + ", id: " + id);
        }

        if (StrUtil.isBlank(parameterType)) {
            throw new XmlParseException("sql 操作节点中的 parameterType 属性不能为空, Mapper: " + this.namespace + ", id: " + id);
        }

        if (StrUtil.isBlank(resultType)) {
            throw new XmlParseException("sql 操作节点中的 resultType 属性不能为空, Mapper: " + this.namespace + ", id: " + id);
        }

        if (StrUtil.isBlank(sql)) {
            throw new XmlParseException("sql 操作节点中的 sql 内容不能为空, Mapper: " + this.namespace + ", id: " + id);
        }

        MapperStatement ms = new MapperStatement(buildMapperStatementId(id), namespace, parameterType, resultType, sql);
        // todo: 实现 mapper statement registry
        configuration.addMapperStatement(ms);
    }

    private String buildMapperStatementId(String id) {
        return namespace + "." + id;
    }
}
