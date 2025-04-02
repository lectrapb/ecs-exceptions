package com.app.shared.common.domain.exceptions;

import com.app.shared.common.domain.exceptions.ecs.BusinessExceptionECS;

public class BusinessException extends BusinessExceptionECS {

    private String customMjs;

    private final ConstantBusinessException businessException;

    public BusinessException(ConstantBusinessException systemException) {
        super(systemException.getMessage());
        this.businessException = systemException;
    }

    public BusinessException(ConstantBusinessException systemException, String customMjs) {
        super(systemException.getMessage());
        this.businessException = systemException;
        this.customMjs = customMjs;
    }
}
