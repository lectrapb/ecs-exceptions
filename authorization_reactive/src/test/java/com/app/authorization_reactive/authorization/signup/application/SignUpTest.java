package com.app.authorization_reactive.authorization.signup.application;

import com.app.authorization_reactive.authorization.signup.domain.gateway.UserSignUpStorage;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUpData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SignUpTest {

    private UserSignUpStorage userSignUpStorage;
    private SignUp useCase;

    @BeforeEach
    void setUp() {
        userSignUpStorage = mock(UserSignUpStorage.class);
        useCase  = new SignUp(userSignUpStorage);
    }


    @Test
    void execute_null_test() {
        //Given
        var request = new UserSignUpData(null, null, null);
        //When
        when(userSignUpStorage.save(any())).thenReturn(Mono.empty());
        var response = useCase.execute(request);
        //Then
        StepVerifier.create(response)
                .expectError()
                .verify();
    }

    @Test
    void execute_ok_test() {
        //Given
        var request = new UserSignUpData("jhon", "jhon@mail.com", "12345678");
        //When
        when(userSignUpStorage.save(any())).thenReturn(Mono.empty());
        var response = useCase.execute(request);
        //Then
        StepVerifier.create(response)
                .verifyComplete();
    }


}