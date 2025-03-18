package com.app.authorization_reactive.shared.helpers.ecs;


import com.app.authorization_reactive.shared.helpers.ecs.model.LogException;
import com.app.authorization_reactive.shared.helpers.ecs.model.LoggerEcs;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;

public class MiddlewareEcsExcp extends MiddlewareEcsLog {

    private MiddlewareEcsLog next;
    @Override
    public void handler(Throwable request,
                        String service) {
         if(request  instanceof Exception exp){

             var errorLog = LogException.ErrorLog.builder()
                     .type(exp.getClass().getName())
                     .description( exp.getMessage())
                     .message(exp.getMessage())
                     .build();
             var logExp = LogException.builder()
                     .service("")
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
