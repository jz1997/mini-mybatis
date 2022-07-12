package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.builder.SqlSourceBuilder;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.session.SqlSource;
import com.exmaple.small.mybatis.xml.XmlStatement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MappedStatement {
    private Configuration configuration;

    // namespace + method name
    private String id;

    /**
     * 配置文件的名称, 例如 UserMapper.xml
     */
    private String resource;

    /**
     *
     */
    private SqlSource sqlSource;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 返回值类型
     */
    private String resultType;

    /**
     * SELECT、INSERT、UPDATE、DELETE
     */
    private String sqlCommandType;

    public MappedStatement(Configuration configuration, XmlStatement xmlStatement) {
        try {
            this.configuration = configuration;
            this.id = xmlStatement.getNamespace() + "." + xmlStatement.getId();
            this.resource = xmlStatement.getNamespace();
            this.sqlSource =
                    new SqlSourceBuilder(configuration)
                            .parse(xmlStatement.getOriginalSql(), Class.forName(xmlStatement.getParameterType()));
            this.parameterType = xmlStatement.getParameterType();
            this.resultType = xmlStatement.getResultType();
            this.sqlCommandType = xmlStatement.getSqlCommandType();
        } catch (ClassNotFoundException e) {
            log.error("Can not load parameter class: {}, Cause: {}", xmlStatement.getParameterType(), e);
            throw new RuntimeException(e);
        }
    }

    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }
}
