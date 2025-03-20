package com.app.authorization_reactive.shared.helpers.ecs;


import com.app.authorization_reactive.shared.common.domain.exception.ecs.BusinessExceptionECS;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogRecord;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;

import java.util.Map;

public class MiddlewareEcsBusiness extends MiddlewareEcsLog {

    private MiddlewareEcsLog ecs;

    @Override
    protected void process(Object request, String service) {
        if(request instanceof BusinessExceptionECS exp){
            LogRecord.ErrorLog<String, String> errorLog = new LogRecord.ErrorLog<>();
            errorLog.setType(exp.getConstantBusinessException().getLogCode());
            errorLog.setDescription(exp.getConstantBusinessException().getInternalMessage());
            errorLog.setMessage(exp.getConstantBusinessException().getMessage());
            errorLog.setOptionalInfo(exp.getOptionalInfo());

            LogRecord<String, String> logExp = new LogRecord<>();
            logExp.setMessageId(exp.getMetaInfo().getMessageId());
            logExp.setService(service);
            logExp.setError(errorLog);
            logExp.setLevel(LogRecord.Level.ERROR);

            LoggerEcs.print(logExp);
        } else if(ecs != null){
            ecs.handle(request, service);
        }
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.ecs = next;
        return this;
    }
}
