package com.app.application;

import com.app.domain.model.Relationship;
import com.app.domain.model.gateway.RelationGateway;
import com.app.domain.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateRelationTest {

    private  RelationGateway repository;
    private  CreateRelation useCase;

    @BeforeEach
    void setUp() {
        repository = mock(RelationGateway.class);
        useCase = new CreateRelation(repository);
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