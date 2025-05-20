package com.app.createtrustrelation.infra.adapter.database.application;

import com.app.createtrustrelation.domain.model.TrustRelation;
import com.app.createtrustrelation.domain.gateway.TrustRelationGateway;
import com.app.createtrustrelation.infra.adapter.database.infra.RelationshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RelationshipSave implements TrustRelationGateway {

    private final RelationshipRepository repository;
    @Override
    public Mono<Void> save(TrustRelation relationship) {

        var trustRelationData = MapRelations.toData(relationship);
        return repository.save(trustRelationData)
                .flatMap(signalType -> Mono.empty());
    }
}
