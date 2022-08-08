package com.mini.mybatis.xml;

public class MapperStatement {
    // namespace + "." + id
    private String id;

    // com.mini.mybatis.mapper.UserMapper
    private String namespace;

    // sql content
    private String sql;

    private String parameterType;

    private String resultType;

    public MapperStatement(String id, String namespace, String parameterType, String resultType, String sql) {
        this.id = id;
        this.namespace = namespace;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sql = sql;
    }
}
