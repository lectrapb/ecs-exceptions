package com.app.authorization_reactive.authorization.signup.infra.adapter.database.application;

import com.app.authorization_reactive.authorization.signup.domain.gateway.UserSignUpStorage;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSignUpStorageAdapter implements UserSignUpStorage {

    List<UserSignUp> users = new ArrayList<>();

    @Override
    public Mono<Void> save(UserSignUp userSignUp) {
        users.add(userSignUp);
        return Mono.empty() ;
    }
}
