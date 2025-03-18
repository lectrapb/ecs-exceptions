package com.app.authorization_reactive.shared.helpers.ecs;


import com.app.authorization_reactive.shared.helpers.ecs.model.LogException;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;

public class MiddlewareEcsTrow extends MiddlewareEcsLog {

    private MiddlewareEcsLog next;
    @Override
    public void handler(Throwable request,
                        String service) {
         if(request != null){

             var errorLog = LogException.ErrorLog.builder()
                     .type( request.getClass().getName())
                     .description(  request.getMessage())
                     .message(request.getMessage())
                     .build();
             var logExp = LogException.builder()
                     .service(service)
                     .error(errorLog)
                     .level(LogException.Level.FATAL)
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
