package com.app.authorization_reactive.shared.common.infra.rest.domain;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.util.annotation.NonNull;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBufferUtils;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

	private final Flux<DataBuffer> cachedBody;
	private final AtomicReference<String> cachedBodyString = new AtomicReference<>("");

	public RequestLoggingDecorator(ServerHttpRequest delegate, DataBufferFactory bufferFactory) {
		super(delegate);

		Flux<DataBuffer> body = super.getBody();
		this.cachedBody = DataBufferUtils.join(body)
			.flatMapMany(dataBuffer -> {
				try {
					byte[] bytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(bytes);
					DataBufferUtils.release(dataBuffer);
					cachedBodyString.set(new String(bytes, StandardCharsets.UTF_8));
					return Flux.just(bufferFactory.wrap(bytes));
				} catch (Exception e) {
					log.error("Error reading request body", e);
					return Flux.empty();
				}
			});
	}

	@Override
	@NonNull
	public Flux<DataBuffer> getBody() {
		return cachedBody;
	}

	public String getBodyAsString() {
		return cachedBodyString.get();
	}
}