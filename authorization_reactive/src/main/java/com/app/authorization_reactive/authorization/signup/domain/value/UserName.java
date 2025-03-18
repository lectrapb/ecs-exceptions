package com.app.authorization_reactive.authorization.signup.domain.value;


import com.app.authorization_reactive.authorization.shared.domain.exception.BusinessException;

public class UserName {

    private final  String value;

    public UserName(String value) {

        if(value == null || value.isEmpty()){
            throw new BusinessException("value username not allowed");
        }
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
