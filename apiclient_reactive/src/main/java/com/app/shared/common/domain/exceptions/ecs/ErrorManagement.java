package com.app.shared.common.domain.exceptions.ecs;

public interface ErrorManagement {

    Integer getStatus() ;
    String getMessage();
    String getErrorCode();
    String getInternalMessage();
    String getLogCode();
}
