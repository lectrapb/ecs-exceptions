package com.app.authorization_reactive.shared.common.domain.exception;

import com.app.authorization_reactive.shared.common.domain.exception.ecs.BusinessExceptionECS;

public class BusinessException extends BusinessExceptionECS {

    public BusinessException(String value) {
       super(value);
    }
}
