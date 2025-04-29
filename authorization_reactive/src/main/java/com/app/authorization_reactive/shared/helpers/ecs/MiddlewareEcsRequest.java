package com.app.authorization_reactive.shared.helpers.ecs;

import com.app.authorization_reactive.shared.helpers.ecs.model.LogMetricRequest;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogMetrics;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogRecord;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;

public class MiddlewareEcsRequest extends MiddlewareEcsLog {

    private MiddlewareEcsLog ecs;

    @Override
    protected void process(Object request, String service) {
        if (request instanceof LogMetricRequest requestInfo) {
            LogMetrics<String, String> logRecord = new LogMetrics<>();
            logRecord.setMessageId(requestInfo.getMessageId());
            logRecord.setService(service);
            logRecord.setLevel(LogRecord.Level.INFO);
            logRecord.setConsumer(requestInfo.getConsumer());

            LogMetrics.AdditionalInfo<String, String> additionalInfo = buildAdditionalInfo(requestInfo);
            logRecord.setAdditionalInfo(additionalInfo);

            LoggerEcs.print(logRecord);
        } else if(ecs != null){
            ecs.handle(request, service);
        }
    }

    private LogMetrics.AdditionalInfo<String, String> buildAdditionalInfo(LogMetricRequest requestInfo) {
        LogMetrics.AdditionalInfo<String, String> additionalInfo = new LogMetrics.AdditionalInfo<>();
        additionalInfo.setRequestBody(requestInfo.getRequestBody());
        additionalInfo.setResponseBody(requestInfo.getResponseBody());
        additionalInfo.setResponseResult(requestInfo.getResponseResult());
        additionalInfo.setResponseCode(String.valueOf(requestInfo.getResponseCode()));
        additionalInfo.setMethod(requestInfo.getMethod());
        additionalInfo.setUri(requestInfo.getUrl());
        return additionalInfo;
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.ecs = next;
        return this;
    }
}

