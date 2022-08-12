package com.mini.mybatis.mapping;

public class MappedStatement {
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
    private String sql;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 返回值类型
     */
    private String resultType;

    public MappedStatement(String id, String namespace, String parameterType, String resultType, String sql) {
        this.id = id;
        this.namespace = namespace;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sql = sql;
    }


    public String getId() {
        return id;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getSql() {
        return sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getResultType() {
        return resultType;
    }
}
