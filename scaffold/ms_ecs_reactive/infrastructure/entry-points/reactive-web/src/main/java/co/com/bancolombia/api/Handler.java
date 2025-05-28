package co.com.bancolombia.api;

import co.com.bancolombia.api.response.HandlerResponse;
import co.com.bancolombia.model.utils.Constants;
import co.com.bancolombia.usecase.ExampleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ExampleUseCase useCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return useCase.getItems().collectList().flatMap( items ->
            HandlerResponse.createSuccessResponse(items, HttpStatus.OK.value(),
            serverRequest.headers().firstHeader(Constants.MESSAGE_ID_HEADER)));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return useCase.getMap().flatMap( map ->
            HandlerResponse.createSuccessResponse(map, HttpStatus.OK.value(),
            serverRequest.headers().firstHeader(Constants.MESSAGE_ID_HEADER)));
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return useCase.throwException().then(
            HandlerResponse.createSuccessResponse(null, HttpStatus.OK.value(),
            serverRequest.headers().firstHeader(Constants.MESSAGE_ID_HEADER)));
    }
}
