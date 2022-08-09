package com.mini.mybatis.exception;

public class XmlParseException extends RuntimeException {
    public XmlParseException(String message) {
        super(message);
    }

    public XmlParseException(String message, Exception e) {
        super(message, e);
    }
}
