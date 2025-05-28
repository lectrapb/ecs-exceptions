package co.com.bancolombia.api.response;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class HandlerResponse {
    private HandlerResponse() {
        throw new IllegalStateException("Utility class");
    }

    public static Mono<ServerResponse> createSuccessResponse(Object bodyResponse, int status, String messageId) {
        return ApiResponse.createOnSuccess(bodyResponse, messageId)
                .flatMap(apiResponse -> ServerResponse.status(status)
                        .body(Mono.just(apiResponse), ApiResponse.class));
    }
}
