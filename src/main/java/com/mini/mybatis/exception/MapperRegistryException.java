package com.mini.mybatis.exception;

public class MapperRegistryException extends RuntimeException {
    public MapperRegistryException(String message) {
        super(message);
    }

    public MapperRegistryException(String message, Exception e) {
        super(message, e);
    }
}
