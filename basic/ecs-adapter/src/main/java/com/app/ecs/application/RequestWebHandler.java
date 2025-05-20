package com.app.ecs.application;

import com.app.ecs.domain.Ecs;
import com.app.ecs.infra.config.EcsPropertiesConfig;
import com.app.ecs.infra.decorators.RequestLoggingDecorator;
import com.app.ecs.infra.decorators.ResponseLoggingDecorator;
import com.app.ecs.helpers.DataSanitizer;
import com.app.ecs.model.management.BusinessExceptionECS;
import com.app.ecs.model.request.LogRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestWebHandler implements WebFilter {
    public static final String RAW_BODY = "raw";
    public static final Set<String> CONSUMER_ACRONYMS = Set.of("consumer-acronym", "code", "channel");
    public static final String MESSAGE_ID = "message-id";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final EcsPropertiesConfig ecsPropertiesConfig;
    private final Boolean showRequestLogs;
    private final Boolean showResponseLogs;

    public RequestWebHandler(EcsPropertiesConfig ecsPropertiesConfig) {
        this.ecsPropertiesConfig = ecsPropertiesConfig;
        this.showRequestLogs = ecsPropertiesConfig.getShowRequestLogs();
        this.showResponseLogs = ecsPropertiesConfig.getShowResponseLogs();
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (ecsPropertiesConfig.getExcludedPaths().stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        if (Boolean.FALSE.equals(showRequestLogs) && Boolean.FALSE.equals(showResponseLogs)) {
            return chain.filter(exchange);
        }

        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

        RequestLoggingDecorator decoratedRequest = new RequestLoggingDecorator(
            exchange.getRequest(), bufferFactory);

        ResponseLoggingDecorator decoratedResponse = new ResponseLoggingDecorator(
            exchange.getResponse(), bufferFactory
        );

        ServerWebExchange mutatedExchange = exchange
            .mutate()
            .request(decoratedRequest)
            .response(decoratedResponse)
            .build();

        return chain.filter(mutatedExchange)
            .then(Mono.defer(() -> logRequest(decoratedRequest, decoratedResponse)))
            .onErrorResume(error ->
                Mono.defer(() -> logError(error, decoratedRequest)));
    }

    private Mono<Void> logRequest(RequestLoggingDecorator decoratedRequest,
                                  ResponseLoggingDecorator decoratedResponse) {

        var requestInfo = new LogRequest();

        setRequestParameters(decoratedRequest, requestInfo);

        if (Boolean.TRUE.equals(showRequestLogs)) {
            sensitiveRequestBody(decoratedRequest, requestInfo);

        }

        if (Boolean.TRUE.equals(showResponseLogs)) {
            sensitiveResponseBody(decoratedResponse, requestInfo);
        }

        return Ecs.build(requestInfo, ecsPropertiesConfig.getServiceName());
    }

    private Mono<Void> logError(Throwable error, RequestLoggingDecorator decoratedRequest) {

        var requestInfo = new LogRequest();

        setRequestParameters(decoratedRequest, requestInfo);

        sensitiveRequestBody(decoratedRequest, requestInfo);

        // Response
        HttpStatus status = resolveHttpStatus(error);
        requestInfo.setResponseCode(String.valueOf(status.value()));
        requestInfo.setResponseResult(status.getReasonPhrase());

        requestInfo.setError(error);

        return Ecs.build(requestInfo, ecsPropertiesConfig.getServiceName())
            .then(Mono.error(error));
    }

    private void sensitiveResponseBody(ResponseLoggingDecorator decoratedResponse, LogRequest requestInfo) {
        // Response
        HttpStatus status = (HttpStatus) Objects.requireNonNull(decoratedResponse.getStatusCode());
        requestInfo.setResponseCode(String.valueOf(status.value()));
        requestInfo.setResponseResult(status.getReasonPhrase());

        var sensitiveResponseFields = ecsPropertiesConfig.getSensitiveResponseFields();
        var sensitiveResponsePatterns = ecsPropertiesConfig.getSensitiveResponsePatterns();
        var sensitiveResponseReplacement = ecsPropertiesConfig.getSensitiveResponseReplacement();

        String sanitizedResponse = DataSanitizer.sanitize(
            decoratedResponse.getBodyAsString(), sensitiveResponseFields, sensitiveResponsePatterns,
            sensitiveResponseReplacement);

        requestInfo.setResponseBody(parseToMap(sanitizedResponse));
    }

    private void sensitiveRequestBody(RequestLoggingDecorator decoratedRequest, LogRequest requestInfo) {
        // Request
        var sensitiveRequestFields = ecsPropertiesConfig.getSensitiveRequestFields();
        var sensitiveRequestPatterns = ecsPropertiesConfig.getSensitiveRequestPatterns();
        var sensitiveRequestReplacement = ecsPropertiesConfig.getSensitiveRequestReplacement();

        String sanitizedRequest = DataSanitizer.sanitize(
            decoratedRequest.getBodyAsString(), sensitiveRequestFields, sensitiveRequestPatterns,
            sensitiveRequestReplacement);

        requestInfo.setRequestBody(parseToMap(sanitizedRequest));
    }

    private void setRequestParameters(RequestLoggingDecorator decoratedRequest, LogRequest requestInfo) {
        requestInfo.setMethod(decoratedRequest.getMethod().name());
        requestInfo.setUrl(decoratedRequest.getURI().getPath());

        Map<String, String> headers = DataSanitizer.sanitizeHeaders(decoratedRequest.getHeaders().entrySet(),
            ecsPropertiesConfig.getAllowRequestHeaders());
        setConsumer(requestInfo, headers);
        requestInfo.setMessageId(headers.get(MESSAGE_ID));
        requestInfo.setHeaders(headers);
    }

    private static void setConsumer(LogRequest requestInfo, Map<String, String> headers) {
        var consumer = CONSUMER_ACRONYMS.stream()
            .map(headers::get)
            .filter(Objects::nonNull)
            .findFirst().orElse(null);
        requestInfo.setConsumer(consumer);
    }

    private HttpStatus resolveHttpStatus(Throwable ex) {
        if (ex instanceof BusinessExceptionECS businessExceptionECS) {
            return HttpStatus.valueOf(businessExceptionECS.getConstantBusinessException().getStatus());
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


    private Map<String, String> parseToMap(String body) {
        try {
            return objectMapper.readValue(body, Map.class);
        } catch (Exception e) {
            return Map.of(RAW_BODY, body);
        }
    }
}
