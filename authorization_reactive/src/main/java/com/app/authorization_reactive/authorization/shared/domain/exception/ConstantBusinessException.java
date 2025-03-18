package com.app.authorization_reactive.authorization.shared.domain.exception;

import com.app.authorization_reactive.authorization.shared.domain.exception.ecs.ErrorManagment;

import static java.net.HttpURLConnection.HTTP_CONFLICT;

public enum ConstantBusinessException implements ErrorManagment {
    DEFAULT_EXCEPTION(HTTP_CONFLICT,
            CodeMessage.INITIAL_CODE_DETAIL,
            BusinessCode.BASIC_INITIAL_CODE,
            InternalMessage.TECHNICAL_ERROR,
            CodeLog.LOG404_00);

    private final Integer status;
    private final String message;
    private final String errorCode;
    private final String internalMessage;
    private final String logCode;

    ConstantBusinessException(Integer status, String message, String errorCode, String internalMessage,
                              String logCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.internalMessage = internalMessage;
        this.logCode = logCode;
    }

    private static class CodeMessage {
        public static final String INITIAL_CODE_DETAIL = "Pendiente implementacion ECS";

    }

    private static class BusinessCode {
        public static final String BASIC_INITIAL_CODE  = "ER404-00";
    }

    private static class CodeLog {
        public static final String LOG404_00 = "LOG404-000";
        public static final String LOG400_03 = "LOG400-03";
    }

    private static class InternalMessage {
        public static final String TECHNICAL_ERROR = "Error técnico";
        public static final String BUSINESS_EXCEPTION = "Error de Negocio";
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getInternalMessage() {
        return internalMessage;
    }

    @Override
    public String getLogCode() {
        return logCode;
    }
}
