package com.app;
/**
 * In this example shows how to manage four types of exception, two are
 * personalized exception BusinessException and AppException both are child
 * of Runtime exception
 */

import com.app.ecs.Ecs;
import com.app.exception.AppException;
import com.app.exception.BusinessException;
import com.app.exception.ExceptionConstant;

public class Main {

    public static void main(String[] args) {

        var ecs = Ecs.build();
        var businessExp = new BusinessException(ExceptionConstant.MESSAGE_DATA_IS_REQUIRED,
                 ExceptionConstant.CODE_FAULT_403, "Number 1");
        var appExp = new AppException(ExceptionConstant.MESSAGE_USER_IS_PRESENT,
                ExceptionConstant.CODE_FAULT_404, "Number 2");

        ecs.handler(businessExp);
        ecs.handler(appExp);

        try{
            zeroDivision();
        }catch(Exception ex){
            ecs.handler(ex);
        }

        int[] numbers = {1, 2, 3};
        try{
            var num = numbers[3];
        }catch(Throwable ex){
            ecs.handler(ex);
        }

    }

    public static int zeroDivision() throws Exception {
        return 10 / 0;
    }
}
