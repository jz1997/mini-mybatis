package com.mini.mybatis.mapping;

import com.mini.mybatis.exception.MappedStatementException;

import java.util.HashMap;
import java.util.Map;

public class MappedStatementRegistry {
    private final Map<String, MappedStatement> msMap = new HashMap<>();

    public void addMappedStatement(MappedStatement ms) {
        String key = ms.getId();

        if (hasMappedStatement(key)) {
            throw new MappedStatementException("MappedStatement '" + key + "' 已经注册");
        }

        msMap.put(key, ms);
    }

    public MappedStatement getMappedStatement(String key) {
        if (hasMappedStatement(key)) {
            return msMap.get(key);
        }
        throw new MappedStatementException("MappedStatement '" + key + "' 不存在");
    }

    public boolean hasMappedStatement(String key) {
        return msMap.containsKey(key);
    }
}
