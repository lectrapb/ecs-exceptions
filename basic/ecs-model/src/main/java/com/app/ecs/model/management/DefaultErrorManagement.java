package com.app.ecs.model.management;

public class DefaultErrorManagement implements ErrorManagement {
    @Override
    public Integer getStatus() {
        return 500;
    }

    @Override
    public String getMessage() {
        return "Ha ocurrido un error interno en el servicio.";
    }

    @Override
    public String getErrorCode() {
        return "ER500-01";
    }

    @Override
    public String getInternalMessage() {
        return "";
    }

    @Override
    public String getLogCode() {
        return "";
    }
}

