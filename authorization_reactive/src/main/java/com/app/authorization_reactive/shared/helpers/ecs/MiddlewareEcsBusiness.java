package com.app.authorization_reactive.shared.helpers.ecs;


import com.app.authorization_reactive.shared.common.domain.exception.ecs.BusinessExceptionECS;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogException;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;

public class MiddlewareEcsBusiness extends MiddlewareEcsLog {

    private MiddlewareEcsLog next;

    @Override
    public void handler(Throwable request,
                        String service) {
         if(request instanceof BusinessExceptionECS exp){

             var errorLog = LogException.ErrorLog.builder()
                     .type(exp.getConstantBusinessException().getLogCode())
                     .description(exp.getConstantBusinessException().getInternalMessage())
                     .message(exp.getConstantBusinessException().getMessage())
                     .optionalInfo(exp.getOptionalInfo())
                     .build();

             var logExp = LogException.builder()
                     .messageId(exp.getMetaInfo().getMessageId())
                     .service(service)
                     .error(errorLog)
                     .level(LogException.Level.ERROR)
                     .build();

             LoggerEcs.print(logExp);

         }else if(next != null){
             next.handler(request, service);
         }
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.next = next;
        return this;
    }
}
