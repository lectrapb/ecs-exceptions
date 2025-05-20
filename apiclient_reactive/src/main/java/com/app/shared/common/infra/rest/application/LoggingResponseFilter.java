package com.app.shared.common.infra.rest.application;
//LoggingResponseFilter


import com.app.shared.common.domain.exceptions.ecs.model.LogMetricRequest;
import com.app.shared.common.infra.rest.domain.RequestLoggingDecorator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingResponseFilter implements WebFilter {
    private static final String SERVICE_NAME = "ms_authorization_reactive";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String RAW_BODY = "raw_body";
    public static final String DELIMITER = ",";
    public static final String CONSUMER_ACRONYM = "consumer-acronym";
    public static final String MESSAGE_ID = "message-id";

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "Authorization", "Cookie", "Set-Cookie", "X-API-Key"
    );

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        return logRequestBody(exchange)
                .flatMap(requestBody -> {
                    ServerHttpRequest decoratedRequest = new RequestLoggingDecorator(
                            exchange.getRequest(), bufferFactory, requestBody);

                    return chain.filter(exchange.mutate().request(decoratedRequest).build())
                            .then(Mono.defer(() -> logRequest(exchange.getRequest(), requestBody)));
                });
    }

    private Mono<String> logRequestBody(ServerWebExchange exchange) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .publishOn(Schedulers.boundedElastic())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                });
    }

    private Mono<Void> logRequest(ServerHttpRequest request, String requestBody) {
        var requestInfo = new LogMetricRequest();
        requestInfo.setMethod(request.getMethod().name());
        requestInfo.setUrl(request.getURI().getPath());
       // var body  = DataSanitizer.sanitize(requestBody);
        var body  = requestBody;
        requestInfo.setBody(parseToMap(body));
        System.out.printf("Data print %s",body);

        Map<String, String> headers = new HashMap<>();
        request.getHeaders().forEach((key, value) -> {
            if (!SENSITIVE_HEADERS.contains(key)) {
                headers.put(key, String.join(DELIMITER, value));
            } else {
                headers.put(key, DataSanitizer.REPLACEMENT);
            }
        });

        requestInfo.setConsumer(headers.get(CONSUMER_ACRONYM));
        requestInfo.setMessageId(headers.get(MESSAGE_ID));
       // return Ecs.build(requestInfo, SERVICE_NAME);
        return Mono.empty();
    }

    private Map<String, String> parseToMap(String body) {
        try {
            return objectMapper.readValue(body, Map.class);
        } catch (Exception e) {
            return Map.of(RAW_BODY, body);
        }
    }
}
