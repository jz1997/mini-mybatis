package com.mini.mybatis.exception;

public class DataSourceException extends RuntimeException {
    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Exception e) {
        super(message, e);
    }
}
