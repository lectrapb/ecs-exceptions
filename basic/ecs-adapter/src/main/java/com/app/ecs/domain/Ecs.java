package com.app.ecs.domain;


import com.app.ecs.domain.middleware.MiddlewareEcsBusiness;
import com.app.ecs.domain.middleware.MiddlewareEcsExcp;
import com.app.ecs.domain.model.MiddlewareEcsLog;
import com.app.ecs.domain.middleware.MiddlewareEcsRequest;
import com.app.ecs.domain.middleware.MiddlewareEcsTrow;
import com.app.ecs.model.request.LogRequest;
import reactor.core.publisher.Mono;

public final class Ecs {

    private Ecs() {
    }

    public static MiddlewareEcsLog build() {
        var ecsBusiness = new MiddlewareEcsBusiness();
        var ecsExp = new MiddlewareEcsExcp();
        var ecsTrow = new MiddlewareEcsTrow();
        var ecsRequest = new MiddlewareEcsRequest();
        return ecsRequest.setNext(ecsBusiness.setNext(ecsExp.setNext(ecsTrow)));
    }

    public static Mono<Throwable> build(Throwable throwable,
                                        String service) {
        build().handler(throwable, service);
        return Mono.just(throwable);
    }

    public static Mono<Void> build(LogRequest request, String service) {
        build().handler(request, service);
        return Mono.empty();
    }
}
