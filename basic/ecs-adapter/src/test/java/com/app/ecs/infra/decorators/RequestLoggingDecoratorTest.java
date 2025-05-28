package com.app.ecs.infra.decorators;

import com.app.ecs.infra.decorators.RequestLoggingDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestLoggingDecoratorTest {

    private ServerHttpRequest request;
    private DefaultDataBufferFactory bufferFactory;

    @BeforeEach
    void setUp() {
        request = mock(ServerHttpRequest.class);
        bufferFactory = new DefaultDataBufferFactory();
    }

    @Test
    void testGetBodyAndBodyAsString() {
        String testBody = "{\"password\":\"1234\",\"name\":\"test\"}";
        DataBuffer buffer = bufferFactory.wrap(testBody.getBytes(StandardCharsets.UTF_8));
        when(request.getBody()).thenReturn(Flux.just(buffer));
        RequestLoggingDecorator decorator = new RequestLoggingDecorator(request, bufferFactory);

        StepVerifier.create(decorator.getBody())
            .expectNextMatches(data -> {
                byte[] bytes = new byte[data.readableByteCount()];
                data.read(bytes);
                String content = new String(bytes, StandardCharsets.UTF_8);
                return testBody.equals(content);
            })
            .verifyComplete();

        assertEquals(testBody, decorator.getBodyAsString());
    }
}

