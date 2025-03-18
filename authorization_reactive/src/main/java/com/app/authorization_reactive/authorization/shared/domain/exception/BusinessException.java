package com.app.authorization_reactive.authorization.shared.domain.exception;

import com.app.authorization_reactive.authorization.shared.domain.exception.ecs.BusinessExceptionECS;

public class BusinessException extends BusinessExceptionECS {

    public BusinessException(String value) {
       super(value);
    }
}
