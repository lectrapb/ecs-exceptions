package com.app.domain.shared.exception;

public interface ErrorManagement {

    Integer getStatus() ;
    String getMessage();
    String getErrorCode();
    String getInternalMessage();
    String getLogCode();
}
