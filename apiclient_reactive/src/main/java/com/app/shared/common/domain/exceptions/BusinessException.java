package com.app.shared.common.domain.exceptions;

public class BusinessException extends  RuntimeException{

    private String customMjs;

    private final ConstantSystemException systemException;

    public BusinessException(ConstantSystemException systemException) {
        super(systemException.getMessage());
        this.systemException = systemException;
    }

    public BusinessException(ConstantSystemException systemException, String customMjs) {
        super(systemException.getMessage());
        this.systemException = systemException;
        this.customMjs = customMjs;
    }
}
