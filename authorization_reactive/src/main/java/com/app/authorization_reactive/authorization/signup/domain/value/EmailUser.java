package com.app.authorization_reactive.authorization.signup.domain.value;

import com.app.authorization_reactive.shared.common.domain.exception.BusinessException;

public class EmailUser {

    private String value;

    public EmailUser(String value) {

        if(value == null || value.isEmpty()){
            throw new BusinessException("value email not allowed");
        }
        this.value = value;
    }
}
