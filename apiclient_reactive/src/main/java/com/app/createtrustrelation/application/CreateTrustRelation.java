package com.app.createtrustrelation.application;


import com.app.createtrustrelation.domain.model.TrustRelation;
import com.app.createtrustrelation.domain.gateway.TrustRelationGateway;
import com.app.shared.common.domain.exceptions.BusinessException;
import com.app.shared.common.domain.labels.UseCase;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.app.shared.common.domain.exceptions.ConstantBusinessException;

@UseCase
@AllArgsConstructor
public class CreateTrustRelation {

    private final TrustRelationGateway repository;

    public Mono<Void> createRelation(String cid, String product, String productId){

        return   Mono.fromCallable(() -> TrustRelation.of(cid, product, productId))
                .flatMap(repository::save)
                .onErrorResume(NullPointerException.class,
                        throwable -> Mono.error(new BusinessException(ConstantBusinessException.DEFAULT_EXCEPTION)));
    }



}
