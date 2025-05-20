package com.app.shared.common.infra.rest.domain;


import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {
    private final Flux<DataBuffer> cachedBody;

    public RequestLoggingDecorator(ServerHttpRequest delegate, DataBufferFactory bufferFactory, String requestBody) {
        super(delegate);
        byte[] bytes = requestBody.getBytes(StandardCharsets.UTF_8);
        this.cachedBody = Flux.just(bufferFactory.wrap(bytes));
    }

    @Override
    @NonNull
    public Flux<DataBuffer> getBody() {
        return cachedBody;
    }
}
