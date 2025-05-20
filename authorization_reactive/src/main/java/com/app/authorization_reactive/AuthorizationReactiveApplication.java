package com.app.authorization_reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( scanBasePackages = "com.app")
public class AuthorizationReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationReactiveApplication.class, args);
    }

}
