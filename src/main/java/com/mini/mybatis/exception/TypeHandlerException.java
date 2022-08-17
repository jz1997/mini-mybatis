package com.mini.mybatis.exception;

public class TypeHandlerException extends RuntimeException {
    public TypeHandlerException(String message) {
        super(message);
    }

    public TypeHandlerException(String message, Exception e) {
        super(message, e);
    }
}
