package com.app.authorization_reactive.authorization.signup.infra.adapter.database.application;

import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUp;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUpData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class UserSignUpStorageAdapterTest {

    private UserSignUpStorageAdapter useCase;

    @BeforeEach
    void setUp() {
        useCase = new UserSignUpStorageAdapter();
    }

    @Test
    void save_ok_test() {
        //Given
        var data = UserSignUp.of("jhon", "jhon@mail.com", "123456");
        //When
        var result = useCase.save(data);
        //Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}