package com.app.authorization_reactive.authorization.signup.infra.entrypoint.rest.application;

import com.app.authorization_reactive.authorization.signup.application.SignUp;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUpData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class SignUpHandler {

    private final SignUp signUp;

    public Mono<ServerResponse> create(ServerRequest request) {

        return  request.bodyToMono(UserSignUpData.class)
                .flatMap(signUp::execute)
                .then(ServerResponse.ok().bodyValue(new SignUpSuccess("ok")));
    }

}

@Data
class SignUpSuccess{

    private String message;

    public SignUpSuccess(String message) {
        this.message = message;
    }
}
