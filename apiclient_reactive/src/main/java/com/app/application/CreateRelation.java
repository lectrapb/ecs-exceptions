package com.app.application;


import com.app.domain.model.Relationship;
import com.app.domain.model.gateway.RelationGateway;
import com.app.domain.shared.exception.BusinessException;
import com.app.domain.shared.exception.ConstantSystemException;
import com.app.shared.UseCase;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@UseCase
@AllArgsConstructor
public class CreateRelation {

    private final  RelationGateway repository;

    public Mono<Void> createRelation(String cid, String product, String productId){

        return   Mono.fromCallable(() -> Relationship.of(cid, product, productId))
                .flatMap(repository::save)
                .onErrorResume(NullPointerException.class,
                        throwable -> Mono.error(new BusinessException(ConstantSystemException.MISSING_PARAMS_CODE)));
    }
}
