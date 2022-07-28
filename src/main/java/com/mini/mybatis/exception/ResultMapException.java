package com.mini.mybatis.exception;

public class ResultMapException extends RuntimeException {
    public ResultMapException(String message) {
        super(message);
    }

    public ResultMapException(String message, Exception e) {
        super(message, e);
    }
}
