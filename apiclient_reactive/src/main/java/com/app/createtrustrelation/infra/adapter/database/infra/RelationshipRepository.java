package com.app.createtrustrelation.infra.adapter.database.infra;

import com.app.createtrustrelation.infra.adapter.database.domain.RelationshipData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RelationshipRepository extends ReactiveCrudRepository<RelationshipData, Integer> {
}
