package com.app.createtrustrelation.domain.gateway;

import com.app.createtrustrelation.domain.model.TrustRelation;
import reactor.core.publisher.Mono;

public interface TrustRelationGateway {
    Mono<Void> save(TrustRelation relationship);
}
