package com.mini.mybatis.mapping;

import com.mini.mybatis.session.Configuration;
import com.mini.mybatis.session.SqlSource;
import com.mini.mybatis.session.SqlSourceBuilder;
import lombok.Getter;

@Getter
public class MappedStatement {

    private Configuration configuration;

    /**
     * Mapper 接口方法中的全路径你
     * com.mini.mybatis.mapper.UserMapper.selectByPrimaryKey
     */
    private String id;

    /**
     * Mapper 接口路径
     * com.mini.mybatis.mapper.UserMapper
     */

    private String namespace;

    /**
     * SQL 语句
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

    public MappedStatement(Configuration configuration, String id, String namespace, String parameterType, String resultType, String originalSql) {
        try {
            this.configuration = configuration;
            this.id = id;
            this.namespace = namespace;
            this.parameterType = parameterType;
            this.resultType = resultType;
            this.sqlSource = new SqlSourceBuilder(configuration).build(originalSql, Class.forName(parameterType));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
