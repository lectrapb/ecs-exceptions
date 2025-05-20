package com.app.ecs.domain.model;

import com.app.ecs.domain.model.MiddlewareEcsLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MiddlewareEcsLogTest {
    static class TestMiddleware extends MiddlewareEcsLog {
        @Override
        protected void process(Object request, String service) {
            // Mockito Test
        }
    }

    @Test
    void testShouldCallProcessAndNext() {
        Object mockRequest = new Object();
        String service = "test-service";

        TestMiddleware first = spy(new TestMiddleware());
        TestMiddleware second = spy(new TestMiddleware());

        first.setNext(second);

        first.handler(mockRequest, service);

        verify(first, times(1)).process(mockRequest, service);
        verify(second, times(1)).process(mockRequest, service);
    }

    @Test
    void testShouldOnlyCallFirstWhenNoNext() {
        Object mockRequest = new Object();
        String service = "test-service";

        TestMiddleware only = spy(new TestMiddleware());

        only.handler(mockRequest, service);

        verify(only, times(1)).process(mockRequest, service);
    }
}
