package com.app.authorization_reactive.authorization.signup.domain.gateway;

import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUp;
import reactor.core.publisher.Mono;

public interface UserSignUpStorage {

    Mono<Void> save(UserSignUp userSignUp);
}
