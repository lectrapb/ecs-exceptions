package com.app.ecs;


import com.app.ecs.model.LogException;
import com.app.ecs.model.LoggerEcs;
import com.app.ecs.model.MiddlewareEcsLog;
import com.app.exception.AppException;

public class MiddlewareEcsApp extends MiddlewareEcsLog {

    private MiddlewareEcsLog next;
    @Override
    public void handler(Throwable request) {
         if( request instanceof AppException exp){
             System.out.println(" is an object of App Exception: "
                     + exp.getCode());
             var errorLog = new LogException.ErrorLog(
                     exp.getCode(),
                     exp.getMessage(),
                     exp.getDetail());
             var logExp = LogException.builder()
                     .service("")
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
