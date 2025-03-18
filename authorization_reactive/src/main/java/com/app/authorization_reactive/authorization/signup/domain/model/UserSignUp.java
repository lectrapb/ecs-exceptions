package com.app.authorization_reactive.authorization.signup.domain.model;

import com.app.authorization_reactive.authorization.signup.domain.value.EmailUser;
import com.app.authorization_reactive.authorization.signup.domain.value.PasswordUser;
import com.app.authorization_reactive.authorization.signup.domain.value.UserName;
import lombok.Getter;

@Getter
public class UserSignUp {

    private final UserName username;
    private final EmailUser email;
    private final PasswordUser password;

    private UserSignUp(String username, String email, String password){
        this.username = new UserName(username);
        this.email = new EmailUser(email);
        this.password = new PasswordUser(password);
    }

    public static UserSignUp of(String username, String email, String password){

        return new UserSignUp(username, email, password);
    }
}
