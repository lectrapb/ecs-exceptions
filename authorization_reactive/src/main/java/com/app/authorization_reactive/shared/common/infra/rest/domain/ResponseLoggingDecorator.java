package com.app.authorization_reactive.shared.common.infra.rest.domain;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Log4j2
public class ResponseLoggingDecorator extends ServerHttpResponseDecorator {

	private final List<byte[]> cacheBody = new ArrayList<>();
	private final DataBufferFactory bufferFactory;

	public ResponseLoggingDecorator(ServerHttpResponse delegate, DataBufferFactory bufferFactory) {
		super(delegate);
		this.bufferFactory = bufferFactory;
	}

	@NonNull
	@Override
	public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
		Flux<DataBuffer> flux = Flux.from(body);

		Flux<DataBuffer> cachedFlux = flux.map(originalBuffer -> {
			byte[] bytes = new byte[originalBuffer.readableByteCount()];
			originalBuffer.read(bytes);
			cacheBody.add(bytes);

			DataBufferUtils.release(originalBuffer);
			return bufferFactory.wrap(bytes);
		});

		return super.writeWith(cachedFlux);
	}

	public String getBodyAsString() {
		return cacheBody.stream()
			.map(chunk -> new String(chunk, StandardCharsets.UTF_8))
			.collect(Collectors.joining());
	}
}

