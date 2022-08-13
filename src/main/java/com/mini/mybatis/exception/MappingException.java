package com.mini.mybatis.exception;

public class MappingException extends RuntimeException {
    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, Exception e) {
        super(message, e);
    }
}
