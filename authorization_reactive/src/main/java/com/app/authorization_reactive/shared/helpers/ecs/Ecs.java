package com.app.authorization_reactive.shared.helpers.ecs;



import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;
import reactor.core.publisher.Mono;

public final class Ecs {

    public static MiddlewareEcsLog build() {

        var ecsBusiness = new MiddlewareEcsBusiness();
        var ecsApp = new MiddlewareEcsApp();
        var ecsExp = new MiddlewareEcsExcp();
        var ecsTrow = new MiddlewareEcsTrow();
        return ecsBusiness.setNext(ecsApp.setNext(ecsExp.setNext(ecsTrow)));

    }

    public static Mono<Throwable> build(Throwable throwable,
                                        String service) {
        build().handler(throwable,
                        service);
        return Mono.just(throwable);
    }


    private Ecs() {
    }
}
