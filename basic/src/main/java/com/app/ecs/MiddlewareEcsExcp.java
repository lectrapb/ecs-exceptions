package com.app.ecs;

import com.app.ecs.model.LogException;
import com.app.ecs.model.LoggerEcs;
import com.app.ecs.model.Mapper;
import com.app.ecs.model.MiddlewareEcsLog;
import com.app.exception.BusinessException;

public class MiddlewareEcsExcp extends MiddlewareEcsLog {

    private MiddlewareEcsLog next;
    @Override
    public void handler(Throwable request) {
         if(request  instanceof Exception exp){

             var errorLog = new LogException.ErrorLog(
                     exp.getClass().getName(),
                     exp.getMessage(),
                     Mapper.getStackTraceAsString(exp));
             var logExp = LogException.builder()
                     .service("")
                     .error(errorLog)
                     .level(LogException.Level.FATAL)
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
