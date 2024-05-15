package com.app.domain.model.gateway;

import com.app.domain.model.Relationship;
import reactor.core.publisher.Mono;

public interface RelationGateway {
    Mono<Void> save(Relationship relationship);
}
