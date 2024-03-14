package com.app.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String code;
    private final String detail;

    public BusinessException(String message,
                             String code,
                             String detail) {

        super(message);
        this.code  = code;
        this.detail = message;
    }
}
