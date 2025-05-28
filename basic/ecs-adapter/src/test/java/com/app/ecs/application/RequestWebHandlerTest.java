package com.app.ecs.application;

import com.app.ecs.application.RequestWebHandler;
import com.app.ecs.infra.config.EcsPropertiesConfig;
import com.app.ecs.infra.config.sensitive.SensitiveRequestProperties;
import com.app.ecs.infra.config.sensitive.SensitiveResponseProperties;
import com.app.ecs.infra.config.service.ServiceProperties;
import com.app.ecs.model.management.BusinessExceptionECS;
import com.app.ecs.model.management.ErrorManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestWebHandlerTest {

    @Mock
    private ServiceProperties serviceProperties;

    @Mock
    private SensitiveRequestProperties sensitiveRequestProperties;

    @Mock
    private SensitiveResponseProperties sensitiveResponseProperties;

    @Mock
    private EcsPropertiesConfig ecsPropertiesConfig;

    @Mock
    private WebFilterChain chain;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @InjectMocks
    private RequestWebHandler webHandler;

    @BeforeEach
    void setUp() {
        webHandler = new RequestWebHandler(ecsPropertiesConfig);
    }

    @Test
    void testShouldSkipExcludedPaths() {
        URI uri = URI.create("/actuator");
        when(exchange.getRequest()).thenReturn(request);
        when(request.getURI()).thenReturn(uri);

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(webHandler.filter(exchange, chain))
            .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    void testShouldSkipWhenLogsAreNoShow() {
        when(ecsPropertiesConfig.getShowRequestLogs()).thenReturn(false);
        when(ecsPropertiesConfig.getShowResponseLogs()).thenReturn(false);

        webHandler = new RequestWebHandler(ecsPropertiesConfig);

        URI uri = URI.create("/api/resource");
        when(exchange.getRequest()).thenReturn(request);
        when(request.getURI()).thenReturn(uri);

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(webHandler.filter(exchange, chain))
            .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    void testShouldLogRequestAndResponse() {
        mocksPropertiesConfig();
        URI uri = URI.create("/api/data");
        mockExchange();

        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);
        when(request.getURI()).thenReturn(uri);
        when(request.getHeaders()).thenReturn(new HttpHeaders());
        when(request.getMethod()).thenReturn(HttpMethod.GET);

        mockBodyFactory();
        when(response.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));

        WebFilterChain mockChain = mock(WebFilterChain.class);
        when(mockChain.filter(any())).thenReturn(Mono.empty());

        StepVerifier.create(webHandler.filter(exchange, mockChain))
            .verifyComplete();
    }

    @Test
    void testShouldHandleErrorAndLogItWithBusinessException() {
        mocksPropertiesConfig();
        URI uri = URI.create("/api/error");

        mockExchange();

        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);
        when(request.getURI()).thenReturn(uri);
        when(request.getHeaders()).thenReturn(new HttpHeaders());
        when(request.getMethod()).thenReturn(HttpMethod.GET);

        mockBodyFactory();

        var businessException = new BusinessExceptionECS(ErrorManagement.DEFAULT_EXCEPTION);

        WebFilterChain mockChain = mock(WebFilterChain.class);
        when(mockChain.filter(any())).thenReturn(Mono.error(businessException));

        StepVerifier.create(webHandler.filter(exchange, mockChain))
            .expectError(BusinessExceptionECS.class)
            .verify();
    }

    @Test
    void testShouldHandleGenericException() {
        mocksPropertiesConfig();
        URI uri = URI.create("/api/error");

        mockExchange();

        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);
        when(request.getURI()).thenReturn(uri);
        when(request.getHeaders()).thenReturn(new HttpHeaders());
        when(request.getMethod()).thenReturn(HttpMethod.GET);

        mockBodyFactory();

        WebFilterChain mockChain = mock(WebFilterChain.class);
        when(mockChain.filter(any())).thenReturn(Mono.error(new RuntimeException("Generic Error")));

        StepVerifier.create(webHandler.filter(exchange, mockChain))
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void shouldParseJsonToMapSuccessfully() {
        String json = "{\"key\":\"value\"}";
        Map<String, String> result = invokeParseToMap(json);
        assertEquals("value", result.get("key"));
    }

    @Test
    void shouldReturnRawBodyWhenJsonIsInvalid() {
        String invalidJson = "not-a-json";
        Map<String, String> result = invokeParseToMap(invalidJson);
        assertEquals("not-a-json", result.get(RequestWebHandler.RAW_BODY));
    }

    private void mockBodyFactory() {
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap("{\"key\":\"value\"}".getBytes());
        Flux<DataBuffer> requestBody = Flux.just(dataBuffer);
        when(request.getBody()).thenReturn(requestBody);
    }

    private void mockExchange() {
        ServerWebExchange.Builder exchangeBuilder = mock(ServerWebExchange.Builder.class);

        when(exchange.mutate()).thenReturn(exchangeBuilder);
        when(exchangeBuilder.request(any(ServerHttpRequest.class))).thenReturn(exchangeBuilder);
        when(exchangeBuilder.response(any(ServerHttpResponse.class))).thenReturn(exchangeBuilder);
        when(exchangeBuilder.build()).thenReturn(exchange);
    }

    private void mocksPropertiesConfig() {
        when(serviceProperties.getName()).thenReturn("test-service");

        when(sensitiveRequestProperties.getDelimiter()).thenReturn("\\|");
        when(sensitiveRequestProperties.getShow()).thenReturn(true);
        when(sensitiveRequestProperties.getAllowHeaders()).thenReturn("message-id|code|channel|acronym-channel");
        when(sensitiveRequestProperties.getFields()).thenReturn("");
        when(sensitiveRequestProperties.getExcludedPaths()).thenReturn("");
        when(sensitiveRequestProperties.getPatterns()).thenReturn("");
        when(sensitiveRequestProperties.getReplacement()).thenReturn("*****");

        when(sensitiveResponseProperties.getDelimiter()).thenReturn("\\|");
        when(sensitiveResponseProperties.getShow()).thenReturn(true);
        when(sensitiveResponseProperties.getFields()).thenReturn("");
        when(sensitiveResponseProperties.getPatterns()).thenReturn("");
        when(sensitiveResponseProperties.getReplacement()).thenReturn("*****");

        ecsPropertiesConfig = new EcsPropertiesConfig(serviceProperties, sensitiveRequestProperties,
            sensitiveResponseProperties);
        webHandler = new RequestWebHandler(ecsPropertiesConfig);
    }

    private Map<String, String> invokeParseToMap(String json) {
        try {
            var method = RequestWebHandler.class.getDeclaredMethod("parseToMap", String.class);
            method.setAccessible(true);
            return (Map<String, String>) method.invoke(webHandler, json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}