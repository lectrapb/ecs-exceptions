package com.app.authorization_reactive.authorization.signup.infra.entrypoint.rest.application;

import com.app.authorization_reactive.authorization.signup.application.SignUp;
import com.app.authorization_reactive.authorization.signup.domain.model.UserSignUpData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class SignUpHandler {

    private final SignUp signUp;

    public Mono<ServerResponse> create(ServerRequest request) {
        return  request.bodyToMono(UserSignUpData.class)
                .map( body -> body)
                .flatMap(signUp::execute)
                .then(ServerResponse.ok().bodyValue(new SignUpSuccess("ok")));
    }

    public Mono<ServerResponse> test(ServerRequest request) {
        return  request.bodyToMono(UserSignUpData.class)
            .map( body -> body)
            .flatMap(signUp::execute)
            .then(ServerResponse.status(HttpStatus.CREATED).bodyValue(new SignUpSuccess("test")));
    }

}

@Data
class SignUpSuccess{
    private String email;
    private String message;
    private String username;
    private String accessKey;
    private String secret;
    private String token;
    private Double number;
    private String documentNumber;
    private String other;
    private Boolean isValid;
    private List<Credential> credentials;

    public SignUpSuccess(String message) {
        this.message = message;
        this.email = message;
        this.username = message;
        this.accessKey = message;
        this.secret = message;
        this.token = message;
        this.isValid = true;
        this.number = Double.valueOf("1000.00");
        this.documentNumber = "1234567890";
        this.other = null;
        this.credentials = List.of( new Credential(1L, message));
    }

    @Data
    static class Credential{
        private Long id;
        private String value;
        private String key;

        public Credential(Long id, String value) {
            this.id = id;
            this.value = value;
            this.key = String.valueOf(id);
        }
    }
}
