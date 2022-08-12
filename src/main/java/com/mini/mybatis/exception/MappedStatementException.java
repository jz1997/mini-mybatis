package com.mini.mybatis.exception;

public class MappedStatementException extends RuntimeException {
    public MappedStatementException(String message) {
        super(message);
    }

    public MappedStatementException(String message, Exception e) {
        super(message, e);
    }
}
