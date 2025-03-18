package com.app.authorization_reactive.authorization.signup.domain.value;

import com.app.authorization_reactive.shared.common.domain.exception.BusinessException;

public record PasswordUser(String value) {


    public PasswordUser(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException("value email is mandatory");
        }
        this.value = value;
    }
}
