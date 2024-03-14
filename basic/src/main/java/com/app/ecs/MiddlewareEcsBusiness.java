package com.app.ecs;


import com.app.ecs.model.LogException;
import com.app.ecs.model.LoggerEcs;
import com.app.ecs.model.MiddlewareEcsLog;
import com.app.exception.BusinessException;

import java.util.UUID;

public class MiddlewareEcsBusiness extends MiddlewareEcsLog {

    private MiddlewareEcsLog next;
    @Override
    public void handler(Throwable request) {
         if(request instanceof BusinessException exp){

             System.out.println(" is an object of BusinessException Exception: "
                     + exp.getCode());

             var errorLog =new LogException.ErrorLog(
                     exp.getCode(),
                     exp.getMessage(),
                     exp.getDetail());
             //Load if it arrives
             var messageId = UUID.randomUUID().toString();
             var service = "api-auth";
             var logExp = LogException.builder()
                     .messageId(messageId)
                     .service(service)
                     .error(errorLog)
                     .level(LogException.Level.ERROR)
                     .build();

             LoggerEcs.print(logExp);

         }else if(next != null){
             next.handler(request);
         }
    }

    @Override
    public MiddlewareEcsLog setNext(MiddlewareEcsLog next) {
        this.next = next;
        return this;
    }
}
