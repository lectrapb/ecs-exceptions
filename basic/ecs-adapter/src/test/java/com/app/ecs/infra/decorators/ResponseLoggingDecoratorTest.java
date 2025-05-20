package com.app.ecs.infra.decorators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseLoggingDecoratorTest {

    private DefaultDataBufferFactory bufferFactory;
    private ServerHttpResponse response;

    @BeforeEach
    void setUp() {
        bufferFactory = new DefaultDataBufferFactory();
        response = mock(ServerHttpResponse.class);
    }

    @Test
    void testWriteWithAndGetBodyAsString() {
        String responseBody = "{\"message\":\"Ok\",\"name\":\"test\"}";
        DataBuffer buffer = bufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        when(response.writeWith(any(Publisher.class))).thenAnswer(invocation -> {
            Publisher<DataBuffer> bodyPublisher = invocation.getArgument(0);
            return Flux.from(bodyPublisher).then();
        });

        ResponseLoggingDecorator decorator = new ResponseLoggingDecorator(response, bufferFactory);
        Mono<Void> result = decorator.writeWith(Flux.just(buffer));

        StepVerifier.create(result).verifyComplete();
        assertEquals(responseBody, decorator.getBodyAsString());
    }
}

