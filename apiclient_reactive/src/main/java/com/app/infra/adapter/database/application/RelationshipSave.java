package com.app.infra.adapter.database.application;

import com.app.domain.model.Relationship;
import com.app.domain.model.gateway.RelationGateway;
import com.app.infra.adapter.database.infra.RelationshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RelationshipSave implements RelationGateway {

    private final RelationshipRepository repository;
    @Override
    public Mono<Void> save(Relationship relationship) {

        return repository.save(MapRelations.toData(relationship))
                .flatMap(signalType -> Mono.empty());
    }
}
