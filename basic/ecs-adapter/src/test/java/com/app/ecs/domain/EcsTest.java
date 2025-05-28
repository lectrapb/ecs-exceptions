package com.app.ecs.domain;

import com.app.ecs.domain.Ecs;
import com.app.ecs.domain.model.MiddlewareEcsLog;
import com.app.ecs.model.management.BusinessExceptionECS;
import com.app.ecs.model.management.ErrorManagement;
import com.app.ecs.model.request.LogRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.lang.reflect.Constructor;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class EcsTest {
    public static final String TEST_SERVICE = "testService";

    @Test
    void testBuildMiddleware() {
        MiddlewareEcsLog middleware = Ecs.build();
        assertNotNull(middleware);
    }

    @Test
    void testEcsPrivateConstructor() throws Exception {
        Constructor<Ecs> constructor = Ecs.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var state = constructor.newInstance();

        Assertions.assertNotNull(state);
    }

    @Test
    void testBuildWithThrowable() {
        Throwable mockThrowable = mock(Throwable.class);

        Throwable result = Ecs.build(mockThrowable, TEST_SERVICE).block();

        assertNotNull(result);
    }

    @Test
    void testBuildWithBusinessException() {
        var mockThrowable = new BusinessExceptionECS(ErrorManagement.DEFAULT_EXCEPTION);

        Throwable result = Ecs.build(mockThrowable, TEST_SERVICE).block();

        assertNotNull(result);
    }

    @Test
    void testBuildWithException() {
        Throwable mockThrowable = mock(Exception.class);

        Throwable result = Ecs.build(mockThrowable, TEST_SERVICE).block();

        assertNotNull(result);
    }

    @Test
    void testBuildRequest() {
        LogRequest request = getLogRequest();
        var response = Ecs.build(request, TEST_SERVICE);
        StepVerifier.create(response)
            .verifyComplete();
    }

    @Test
    void testBuildRequestBusinessError() {
        LogRequest request = getLogRequest();
        var mockThrowable = new BusinessExceptionECS(ErrorManagement.DEFAULT_EXCEPTION);
        request.setError(mockThrowable);
        var response = Ecs.build(request, TEST_SERVICE);
        StepVerifier.create(response)
            .verifyComplete();
    }

    @Test
    void testBuildRequestOtherError() {
        LogRequest request = getLogRequest();
        var mockThrowable = new RuntimeException("error");
        request.setError(mockThrowable);
        var response = Ecs.build(request, TEST_SERVICE);
        StepVerifier.create(response)
            .verifyComplete();
    }

    private static LogRequest getLogRequest() {
        LogRequest request = new LogRequest();
        request.setMessageId("message-id");
        request.setConsumer("consumer");
        request.setMethod("POST");
        request.setUrl("/api/v1/log");
        request.setHeaders(Map.of("key", "value"));
        request.setRequestBody(Map.of("key", "value"));
        request.setRequestBody(Map.of("key", "value"));
        request.setResponseResult("OK");
        request.setResponseCode("200");
        return request;
    }

}
