package com.app.shared.common.infra.rest.application;

import com.app.shared.common.domain.exceptions.BusinessException;
import com.app.shared.common.infra.rest.domain.RestResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;


@Component
//@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler{

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderException);
    }


    private Mono<ServerResponse> renderException(ServerRequest serverRequest) {

        return accessError(serverRequest)
                //.flatMap(throwable -> Ecs.build(throwable,"ms_authorization_reactive"))
                .flatMap(Mono::error)
                .onErrorResume(BusinessException.class, this::businessError  )
                .onErrorResume(this::unknownError)
                .cast(ServerResponse.class);

    }

    public Mono<ServerResponse> businessError(BusinessException exception) {
        return  ServerResponse
                .status(HttpStatus.CONFLICT)
                .body(Mono.just(RestResponse.error(exception)),
                        RestResponse.class);
    }

    public Mono<ServerResponse> unknownError(Throwable exception) {
        var description  = exception.getMessage();
        return  ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(Mono.just(description), String.class);
    }

    private Mono<Throwable> accessError(ServerRequest request) {
        return Mono.just(request)
                .map(this::getError);
    }
}
