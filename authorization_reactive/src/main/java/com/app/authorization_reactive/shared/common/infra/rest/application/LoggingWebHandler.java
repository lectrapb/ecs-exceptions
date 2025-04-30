package com.app.authorization_reactive.shared.common.infra.rest.application;

import com.app.authorization_reactive.shared.common.infra.rest.domain.RequestLoggingDecorator;
import com.app.authorization_reactive.shared.common.infra.rest.domain.ResponseLoggingDecorator;
import com.app.authorization_reactive.shared.common.infra.rest.infra.DataSanitizer;
import com.app.authorization_reactive.shared.common.infra.rest.infra.SensitiveDataConfig;
import com.app.authorization_reactive.shared.helpers.ecs.Ecs;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogMetricRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Map;
import java.util.Objects;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingWebHandler implements WebFilter {
	private static final String SERVICE_NAME = "ms_authorization_reactive";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	public static final String RAW_BODY = "body";
	public static final String CONSUMER_ACRONYM = "consumer-acronym";
	public static final String MESSAGE_ID = "message-id";

	private final SensitiveDataConfig sensitiveDataConfig;

    public LoggingWebHandler(SensitiveDataConfig sensitiveDataConfig) {
        this.sensitiveDataConfig = sensitiveDataConfig;
    }

    @Override
	@NonNull
	public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
		DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
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
			.then(Mono.defer(() -> logRequest(decoratedRequest, decoratedResponse)));
	}

	private Mono<Void> logRequest(RequestLoggingDecorator decoratedRequest,
								  ResponseLoggingDecorator decoratedResponse) {
		var requestInfo = new LogMetricRequest();
		HttpStatus status = (HttpStatus) Objects.requireNonNull(decoratedResponse.getStatusCode());
		requestInfo.setResponseCode(status.value());
		requestInfo.setResponseResult(status.getReasonPhrase());
		requestInfo.setMethod(decoratedRequest.getMethod().name());
		requestInfo.setUrl(decoratedRequest.getURI().getPath());

		var sensitiveRequestFields = sensitiveDataConfig.getSensitiveRequestFields();
		var sensitiveRequestPatterns = sensitiveDataConfig.getSensitiveRequestPatterns();
		var sensitiveRequestReplacement = sensitiveDataConfig.getSensitiveRequestReplacement();

		var sensitiveResponseFields = sensitiveDataConfig.getSensitiveResponseFields();
		var sensitiveResponsePatterns = sensitiveDataConfig.getSensitiveResponsePatterns();
		var sensitiveResponseReplacement = sensitiveDataConfig.getSensitiveResponseReplacement();

		String sanitizedRequest = DataSanitizer.sanitize(
			decoratedRequest.getBodyAsString(), sensitiveRequestFields, sensitiveRequestPatterns,
			sensitiveRequestReplacement);
		String sanitizedResponse = DataSanitizer.sanitize(
			decoratedResponse.getBodyAsString(), sensitiveResponseFields, sensitiveResponsePatterns,
			sensitiveResponseReplacement);

		requestInfo.setRequestBody(parseToMap(sanitizedRequest));
		requestInfo.setResponseBody(parseToMap(sanitizedResponse));

		Map<String, String> headers = DataSanitizer.sanitizeHeaders(
			decoratedRequest.getHeaders(), sensitiveDataConfig.getSensitiveRequestHeaders(),
			SensitiveDataConfig.HEADER_DELIMITER, sensitiveRequestReplacement);
		requestInfo.setConsumer(headers.get(CONSUMER_ACRONYM));
		requestInfo.setMessageId(headers.get(MESSAGE_ID));
		return Ecs.build(requestInfo, SERVICE_NAME);
	}

	private Map<String, String> parseToMap(String body) {
		try {
			return objectMapper.readValue(body, Map.class);
		} catch (Exception e) {
			return Map.of(RAW_BODY, body);
		}
	}
}
