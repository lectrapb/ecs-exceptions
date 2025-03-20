package com.app.authorization_reactive.shared.helpers.ecs;


import com.app.authorization_reactive.shared.helpers.ecs.model.LogRecord;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;

public class MiddlewareEcsTrow extends MiddlewareEcsLog {

    private MiddlewareEcsLog ecs;

    @Override
    protected void process(Object request, String service) {
        if(request != null){
            var exp  = (Throwable) request;
            LogRecord.ErrorLog<String, String> errorLog = new LogRecord.ErrorLog<>();
            errorLog.setType(exp.getClass().getName());
            errorLog.setDescription(exp.getMessage());
            errorLog.setMessage(exp.getMessage());

            LogRecord<String, String> logExp = new LogRecord<>();
            logExp.setService(service);
            logExp.setError(errorLog);
            logExp.setLevel(LogRecord.Level.FATAL);

            LoggerEcs.print(logExp);
        } else if(ecs != null){
            ecs.handle(null, service);
        }
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.ecs = next;
        return this;
    }
}
