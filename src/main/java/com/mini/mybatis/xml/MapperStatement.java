package com.mini.mybatis.xml;

public class MapperStatement {
    // namespace + "." + id
    private String id;

    // mapper/UserMapper.xml
    private String namespace;

    private String parameterType;

    private String resultType;


    // sql content
    private String sql;


    public MapperStatement(String id, String namespace, String parameterType, String resultType, String sql) {
        this.id = id;
        this.namespace = namespace;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.sql = sql;
    }
}
