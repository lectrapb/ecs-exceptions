package com.app.authorization_reactive.authorization.signup.infra.entrypoint.rest.infra;

import com.app.authorization_reactive.authorization.signup.infra.entrypoint.rest.application.SignUpHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class SignUpUserRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(SignUpHandler handler){

        return route()
                .POST("/api/v1/signUp", handler::create)
                .build();

    }
}
