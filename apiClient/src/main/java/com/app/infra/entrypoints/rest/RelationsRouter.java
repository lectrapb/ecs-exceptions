package com.app.infra.entrypoints.rest;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RelationsRouter{

    @Bean
    public RouterFunction<ServerResponse> routes(RelationsHandler handler){

          return route()
                  .POST("/api/relations/create", handler::create)
                  .build();
    }
}
