package com.app.authorization_reactive;

import com.app.ecs.application.RequestWebHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RequestWebHandler.class)
@ComponentScan(basePackages = {"com.app"})
public class AuthorizationReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationReactiveApplication.class, args);
    }

}
