package com.app.application;

import com.app.createtrustrelation.application.CreateTrustRelation;
import com.app.createtrustrelation.domain.gateway.TrustRelationGateway;
import com.app.shared.common.domain.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateRelationTest {

    private TrustRelationGateway repository;
    private CreateTrustRelation useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TrustRelationGateway.class);
        useCase = new CreateTrustRelation(repository);
    }

    @Test
    void createRelationNullTest() {
        //given
        String cid = null;
        String product = null;
        String productId = null;
        //when
        useCase.createRelation(cid, product, productId)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
        //then
    }


    @Test
    void createRelationOkTest() {
        //given
        String cid = "data";
        String product = "test";
        String productId = "123456";
        //when
        when(repository.save(any())).thenReturn(Mono.empty());
        //then
        useCase.createRelation(cid, product, productId)
                .as(StepVerifier::create)
                .verifyComplete();
    }


}