package com.app.authorization_reactive.authorization.signup.application;

import com.app.authorization_reactive.authorization.shared.domain.labels.UseCase;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUp;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUpData;
import com.app.authorization_reactive.authorization.signup.domain.gateway.UserSignUpStorage;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@UseCase
@AllArgsConstructor
public class SignUp {


    private final UserSignUpStorage userSignUpPersist;

    public Mono<Void> execute(UserSignUpData signUpData) {

        return Mono.fromCallable(() -> signUpData)
                .flatMap(userSignUpData -> Mono.just(UserSignUp.of(signUpData.username(),
                        signUpData.email(),
                        userSignUpData.password())))
                .flatMap(userSignUp -> userSignUpPersist.save(userSignUp));
    }

}
