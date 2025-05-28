package co.com.bancolombia.api.response;

import co.com.bancolombia.model.exception.AppException;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.ConstantBusinessException;
import co.com.bancolombia.model.utils.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Slf4j
@Order(-2)
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse extends AbstractErrorWebExceptionHandler {
    private static final Integer LIMIT_LEFT = 2;
    private static final Integer LIMIT_RIGHT = 5;


    public ExceptionResponse(ErrorAttributes errorAttributes,
                             ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::buildErrorResponse);
    }

    private Mono<ServerResponse> buildErrorResponse(ServerRequest request) {
        return accessError(request)
                .flatMap(Mono::error)
                .onErrorResume(BusinessException.class, e -> createResponseFromBusiness(e,
                        request.headers().firstHeader(Constants.MESSAGE_ID_HEADER)))
                .onErrorResume(AppException.class, e -> createResponseFromAppException(e,
                        request.headers().firstHeader(Constants.MESSAGE_ID_HEADER)))
                .onErrorResume(e -> unknownError(request.headers().firstHeader(Constants.MESSAGE_ID_HEADER)))
                .cast(ServerResponse.class);
    }

    public Mono<ServerResponse> createResponseFromBusiness(BusinessException exception, String messageId) {
        return Mono.just(ApiResponse.createOnError(Error.builder()
                        .code(exception.getConstantBusinessException().getErrorCode())
                        .detail(exception.getConstantBusinessException().getMessage())
                        .build(), messageId))
                .flatMap(apiResponse -> ServerResponse
                        .status(exception.getConstantBusinessException().getStatus())
                        .body(apiResponse, ApiResponse.class));
    }

    public Mono<ServerResponse> createResponseFromAppException(AppException exception, String messageId) {
        return exception.getCode() != null
                ? Mono.just(ApiResponse.createOnError(Error.builder()
                        .code(exception.getCode())
                        .detail(exception.getMessage())
                        .build(), messageId))
                .flatMap(apiResponse -> ServerResponse
                        .status(Integer.parseInt(exception.getCode().substring(LIMIT_LEFT, LIMIT_RIGHT)))
                        .body(apiResponse, ApiResponse.class))
                : Mono.just(ApiResponse.createOnError(Error.builder()
                        .code(exception.getCode())
                        .detail(exception.getMessage())
                        .build(), messageId))
                .flatMap(apiResponse -> ServerResponse
                        .status(500)
                        .body(apiResponse, ApiResponse.class));
    }

    public Mono<ServerResponse> unknownError(String messageId) {
        return Mono.just(ApiResponse.createOnError(Error.builder()
                        .code(ConstantBusinessException.DEFAULT_EXCEPTION.getErrorCode())
                        .detail(ConstantBusinessException.DEFAULT_EXCEPTION.getMessage())
                        .build(), messageId))
                .flatMap(apiResponse -> ServerResponse
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .body(apiResponse, ApiResponse.class));
    }

    private Mono<Throwable> accessError(ServerRequest request) {
        return just(request)
                .map(this::getError);
    }
}
