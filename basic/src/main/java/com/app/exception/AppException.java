package com.app.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final String code;
    private final String detail;

    public AppException(String message, String code, String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }
}
