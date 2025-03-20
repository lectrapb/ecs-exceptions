package com.app.authorization_reactive.shared.helpers.ecs;

import com.app.authorization_reactive.shared.helpers.ecs.model.LogRecord;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;

import java.util.Map;

public class MiddlewareEcsResponse extends MiddlewareEcsLog {
    public static final String STATUS_CODE = "status_code";
    private MiddlewareEcsLog ecs;

    @Override
    protected void process(Object request, String service) {
        if (request instanceof Map responseInfo) {
            LogRecord<String, String> logRecord = new LogRecord<>();
            logRecord.setMessageId(responseInfo.get("requestId").toString());
            logRecord.setDate(responseInfo.get("timestamp").toString());
            logRecord.setService(service);
            logRecord.setLevel(LogRecord.Level.INFO);

            LogRecord.ErrorLog<String, String> errorLog = new LogRecord.ErrorLog<>();
            errorLog.setType("RESPONSE");
            errorLog.setMessage("Outgoing response");
            errorLog.setDescription("Status: " + responseInfo.get(STATUS_CODE) + " for " + responseInfo.get("method") + " " + responseInfo.get("url"));

            Map<String, String> responseDetails = Map.of(
                    STATUS_CODE, responseInfo.get(STATUS_CODE).toString(),
                    "response_time_ms", responseInfo.get("response_time").toString()
            );

            errorLog.setOptionalInfo(responseDetails);
            logRecord.setError(errorLog);
            LoggerEcs.print(logRecord);
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

