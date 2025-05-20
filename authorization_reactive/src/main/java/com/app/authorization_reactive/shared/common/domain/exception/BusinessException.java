package com.app.authorization_reactive.shared.common.domain.exception;

import com.app.ecs.model.management.BusinessExceptionECS;

public class BusinessException extends BusinessExceptionECS {

    public BusinessException(String value) {
        super(value);
    }

    public BusinessException(ConstantBusinessException value) {
        super(value);
    }
}
