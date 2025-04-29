package com.app.authorization_reactive.shared.common.infra.rest.application;

import com.app.authorization_reactive.shared.common.infra.rest.domain.RequestLoggingDecorator;
import com.app.authorization_reactive.shared.common.infra.rest.domain.ResponseLoggingDecorator;
import com.app.authorization_reactive.shared.helpers.ecs.Ecs;
import com.app.authorization_reactive.shared.helpers.ecs.model.LogMetricRequest;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingWebHandler implements WebFilter {
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

		String sanitizedRequest = DataSanitizer.sanitize(decoratedRequest.getBodyAsString());
		String sanitizedResponse = DataSanitizer.sanitize(decoratedResponse.getBodyAsString());

		requestInfo.setRequestBody(parseToMap(sanitizedRequest));
		requestInfo.setResponseBody(parseToMap(sanitizedResponse));

		Map<String, String> headers = new HashMap<>();
		decoratedRequest.getHeaders().forEach((key, value) -> {
			if (!SENSITIVE_HEADERS.contains(key)) {
				headers.put(key, String.join(DELIMITER, value));
			} else {
				headers.put(key, DataSanitizer.REPLACEMENT);
			}
		});
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
