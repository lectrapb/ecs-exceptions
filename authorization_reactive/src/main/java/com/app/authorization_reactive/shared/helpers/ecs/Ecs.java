package com.app.authorization_reactive.shared.helpers.ecs;



import com.app.authorization_reactive.shared.helpers.ecs.model.LogMetricRequest;
import com.app.authorization_reactive.shared.helpers.ecs.model.MiddlewareEcsLog;
import reactor.core.publisher.Mono;

public final class Ecs {

    public static MiddlewareEcsLog build() {
        var ecsBusiness = new MiddlewareEcsBusiness();
        var ecsApp = new MiddlewareEcsApp();
        var ecsExp = new MiddlewareEcsExcp();
        var ecsTrow = new MiddlewareEcsTrow();
        var ecsRequest = new MiddlewareEcsRequest();
        return ecsRequest.setNext(ecsBusiness.setNext(ecsApp.setNext(ecsExp.setNext(ecsTrow))));

    }

    public static Mono<Throwable> build(Throwable throwable, String service) {
        build().handle(throwable, service);
        return Mono.just(throwable);
    }

    public static Mono<Void> build(LogMetricRequest request, String service) {
        build().handle(request, service);
        return Mono.empty();
    }

//    public static void buildRequest(Map<String, Object> requestInfo, String service) {
//        MiddlewareEcsLog ecs = build();
//        if (ecs instanceof MiddlewareEcsRequest requestLogger) {
//            requestLogger.handle(requestInfo, service);
//        }
//    }
//
//    public static void buildResponse(Map<String, Object> responseInfo, String service) {
//        MiddlewareEcsLog ecs = build();
//        if (ecs instanceof MiddlewareEcsResponse responseLogger) {
//            responseLogger.handle(responseInfo, service);
//        }
//    }

    private Ecs() {
    }
}
