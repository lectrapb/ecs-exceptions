package com.app.ecs.domain.middleware;


import com.app.ecs.domain.model.LogRecord;
import com.app.ecs.domain.model.LoggerEcs;
import com.app.ecs.domain.model.MiddlewareEcsLog;

public class MiddlewareEcsTrow extends MiddlewareEcsLog {
    private MiddlewareEcsLog next;

    @Override
    public void process(Object request,
                        String service) {
        if (request != null) {
            var exp = (Throwable) request;
            var logError = new LogRecord.ErrorLog<String, String>();
            logError.setDescription(exp.getMessage());
            logError.setMessage(exp.getMessage());
            logError.setType(exp.getClass().getName());

            var logExp = new LogRecord<String, String>();
            logExp.setError(logError);
            logExp.setLevel(LogRecord.Level.ERROR);
            logExp.setService(service);

            LoggerEcs.print(logExp);
        } else if (next != null) {
            next.handler(null, service);
        }
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.next = next;
        return this;
    }
}
