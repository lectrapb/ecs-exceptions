package com.app.authorization_reactive.shared.common.infra.rest.application;

import com.app.authorization_reactive.shared.helpers.ecs.model.LogMetricRequest;
import com.app.authorization_reactive.shared.helpers.ecs.Ecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class LoggingWebHandler implements WebFilter {
    private static final String SERVICE_NAME = "AuthorizationReactive";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "Authorization", "Cookie", "Set-Cookie", "X-API-Key"
    );

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    String requestBody = extractBody(dataBuffer);
                    String sanitizedBody = DataSanitizer.sanitize(requestBody);
                    return logRequest(exchange.getRequest(), sanitizedBody)
                            .then(chain.filter(exchange));
                });
    }

    private String extractBody(DataBuffer dataBuffer) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        return new String(bytes, StandardCharsets.UTF_8).trim();
    }

    private Mono<Void> logRequest(ServerHttpRequest request, String sanitizedBody) {
        var requestInfo = new LogMetricRequest();
        requestInfo.setMethod(request.getMethod().name());
        requestInfo.setUrl(request.getURI().getPath());
        requestInfo.setBody(parseToMap(sanitizedBody));

        Map<String, String> headers = new HashMap<>();
        request.getHeaders().forEach((key, value) -> {
            if (!SENSITIVE_HEADERS.contains(key)) {
                headers.put(key, String.join(",", value));
            } else {
                headers.put(key, DataSanitizer.REPLACEMENT);
            }
        });

        requestInfo.setConsumer(headers.get("consumer-acronym"));
        requestInfo.setMessageId(headers.get("message-id"));

        return Ecs.build(requestInfo, SERVICE_NAME);
    }

    private Map<String, String> parseToMap(String body) {
        try {
            return objectMapper.readValue(body, Map.class);
        } catch (Exception e) {
            return Map.of("raw_body", body);
        }
    }
}