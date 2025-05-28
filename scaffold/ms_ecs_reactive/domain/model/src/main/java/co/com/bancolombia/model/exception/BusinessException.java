package co.com.bancolombia.model.exception;

import co.com.bancolombia.ecs.model.management.BusinessExceptionECS;

public class BusinessException extends BusinessExceptionECS {

    public BusinessException(String value) {
        super(value);
    }

    public BusinessException(ConstantBusinessException value) {
        super(value);
    }
}
