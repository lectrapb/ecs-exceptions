package com.app.infra.entrypoints.rest;

import com.app.application.CreateRelation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Component
@AllArgsConstructor
public class RelationsHandler {

    private CreateRelation createRelation;
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(RelationshipCreateDto.class)
                .flatMap(reqDto -> createRelation.createRelation(
                       reqDto.getCid(),
                       reqDto.getProduct(),
                       reqDto.getProductId()
                )).map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(voidOptional -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .body(Mono.just(""), String.class));
    }
}

@Data
class RelationshipCreateDto{

    private String cid;
    private String product;
    private String productId;
}
