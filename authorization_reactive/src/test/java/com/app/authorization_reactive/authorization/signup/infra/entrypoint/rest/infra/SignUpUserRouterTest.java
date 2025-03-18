package com.app.authorization_reactive.authorization.signup.infra.entrypoint.rest.infra;

import com.app.authorization_reactive.authorization.signup.application.SignUp;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUpData;
import com.app.authorization_reactive.authorization.signup.infra.entrypoint.rest.application.SignUpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebFluxTest
@ContextConfiguration(classes = {SignUpUserRouter.class, SignUpHandler.class})
class SignUpUserRouterTest {


    private WebTestClient webClient;

    @MockitoBean
    private SignUp useCase;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void signup_ok_test(){
        //Given
        var body = new UserSignUpData("jhon", "jhon@mail.com", "23232");
        var path = "/api/v1/signUp";
        var responseToValidate ="{\"message\": \"ok\"}";
        //When
        when(useCase.execute(any())).thenReturn(Mono.empty());
        //Then
        validateRequest(body, path, responseToValidate);
    }


    private <T> void validateRequest(T body, String path, String bodyToValidate){

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectBody()
                .json(bodyToValidate);
    }
}