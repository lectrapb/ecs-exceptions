package com.app.authorization_reactive.shared.helpers.ecs;


import com.app.authorization_reactive.shared.common.domain.exception.AppException;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogRecord;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;

public class MiddlewareEcsApp extends MiddlewareEcsLog {

    private MiddlewareEcsLog ecs;

    @Override
    protected void process(Object request, String service) {
        if( request instanceof AppException exp){
            LogRecord.ErrorLog<String, String> errorLog = new LogRecord.ErrorLog<>();
            errorLog.setType(exp.getCode());
            errorLog.setDescription(exp.getMessage());
            errorLog.setMessage(exp.getMessage());

            LogRecord<String, String> logExp = new LogRecord<>();
            logExp.setService(service);
            logExp.setError(errorLog);
            logExp.setLevel(LogRecord.Level.ERROR);
            LoggerEcs.print(logExp);

            LoggerEcs.print(logExp);
        }else if(ecs != null){
            ecs.handle(request, service);
        }
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.ecs = next;
        return this;
    }
}
