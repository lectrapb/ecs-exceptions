package com.app.shared.common.domain.exceptions;

import com.app.shared.common.domain.exceptions.ecs.ErrorManagement;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public enum ConstantSystemException implements ErrorManagement {

    MISSING_PARAMS_CODE(HTTP_BAD_REQUEST, "Faltan datos requeridos",
                        SysCode.BER404_01, "Valor no puede ser nulo para relacion",
                        SysCode.BER404_01),
    MISSING_PARAMS_CODE_CID(HTTP_BAD_REQUEST, "Faltan datos requeridos",
            SysCode.BER404_01, "Valor no puse ser nulo para relacion",
            LogCode.BE604_01);

    private final Integer status;
    private final String message;
    private final String errorCode;
    private final String internalMessage;
    private final String logCode;



    ConstantSystemException(Integer status, String message, String errorCode,
                            String internalDescription, String logCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.internalMessage = internalDescription;
        this.logCode = logCode;
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


    private static class SysCode{
        public static final String BER404_01 = "BER404-01";
    }

    private static class LogCode{
        public static final String BE604_01 = "BE604_01";
    }
}
