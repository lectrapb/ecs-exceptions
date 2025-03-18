package com.app.authorization_reactive.authorization.shared.domain.exception.ecs;

public interface ErrorManagment {
    Integer getStatus();
    String getMessage();
    String getErrorCode();
    String getInternalMessage();
    String getLogCode();
}
