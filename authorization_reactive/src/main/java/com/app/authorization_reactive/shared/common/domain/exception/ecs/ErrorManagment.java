package com.app.authorization_reactive.shared.common.domain.exception.ecs;

public interface ErrorManagment {
    Integer getStatus();
    String getMessage();
    String getErrorCode();
    String getInternalMessage();
    String getLogCode();
}
