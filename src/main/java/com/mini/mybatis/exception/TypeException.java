package com.mini.mybatis.exception;

public class TypeException extends RuntimeException {
    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Exception e) {
        super(message, e);
    }
}
