package com.app.ecs;


import com.app.ecs.model.MiddlewareEcsLog;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Ecs {

    public static MiddlewareEcsLog build() {

        var ecsBusiness = new MiddlewareEcsBusiness();
        var ecsApp = new MiddlewareEcsApp();
        var ecsExp = new MiddlewareEcsExcp();
        var ecsTrow = new MiddlewareEcsTrow();
        return ecsBusiness.setNext(ecsApp.setNext(ecsExp.setNext(ecsTrow)));

    }

    public static Throwable build(Throwable throwable) {
        build().handler(throwable);
        return throwable;
    }


    private Ecs() {
    }
}
