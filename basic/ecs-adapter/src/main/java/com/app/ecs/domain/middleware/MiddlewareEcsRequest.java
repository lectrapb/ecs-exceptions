package com.app.ecs.domain.middleware;

import com.app.ecs.domain.model.LogRecord;
import com.app.ecs.domain.model.LoggerEcs;
import com.app.ecs.domain.model.MiddlewareEcsLog;
import com.app.ecs.model.management.BusinessExceptionECS;
import com.app.ecs.model.request.LogRequest;

import java.util.Objects;

public class MiddlewareEcsRequest extends MiddlewareEcsLog {
    private MiddlewareEcsLog next;

    @Override
    protected void process(Object request, String service) {
        if (request instanceof LogRequest requestInfo) {
            LogRecord<String, String> logRecord = new LogRecord<>();
            logRecord.setMessageId(requestInfo.getMessageId());
            logRecord.setService(service);
            logRecord.setLevel(LogRecord.Level.INFO);
            logRecord.setConsumer(requestInfo.getConsumer());

            LogRecord.AdditionalInfo<String, String> additionalInfo = buildAdditionalInfo(requestInfo);
            logRecord.setAdditionalInfo(additionalInfo);

            if (Objects.nonNull(requestInfo.getError())) {
                var error =  requestInfo.getError();
                LogRecord.ErrorLog<String, String> optionalMap = new LogRecord.ErrorLog<>();
                var logError = new LogRecord.ErrorLog<String, String>();
                if (error instanceof BusinessExceptionECS exp) {
                    optionalMap.setOptionalInfo(exp.getOptionalInfo());
                    logError.setOptionalInfo(optionalMap.getOptionalInfo());
                    logError.setDescription(exp.getConstantBusinessException().getInternalMessage());
                    logError.setMessage(exp.getConstantBusinessException().getMessage());
                    logError.setType(exp.getConstantBusinessException().getLogCode());
                }
                else{
                    logError.setDescription(error.getMessage());
                    logError.setMessage(error.getMessage());
                    logError.setType(error.getClass().getName());
                }
                logRecord.setError(logError);
                logRecord.setLevel(LogRecord.Level.ERROR);
            }

            LoggerEcs.print(logRecord);
        } else if (next != null) {
            next.handler(request, service);
        }
    }

    private LogRecord.AdditionalInfo<String, String> buildAdditionalInfo(LogRequest requestInfo) {
        LogRecord.AdditionalInfo<String, String> additionalInfo = new LogRecord.AdditionalInfo<>();
        additionalInfo.setHeaders(requestInfo.getHeaders());
        additionalInfo.setRequestBody(requestInfo.getRequestBody());
        additionalInfo.setResponseBody(requestInfo.getResponseBody());
        additionalInfo.setResponseResult(requestInfo.getResponseResult());
        additionalInfo.setResponseCode(requestInfo.getResponseCode());
        additionalInfo.setMethod(requestInfo.getMethod());
        additionalInfo.setUri(requestInfo.getUrl());
        return additionalInfo;
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.next = next;
        return this;
    }
}

