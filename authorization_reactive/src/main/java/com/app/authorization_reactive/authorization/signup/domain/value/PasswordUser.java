package com.app.authorization_reactive.authorization.signup.domain.value;

import com.app.authorization_reactive.shared.common.domain.exception.BusinessException;
import com.app.authorization_reactive.shared.common.domain.exception.ConstantBusinessException;

public record PasswordUser(String value) {

    public PasswordUser {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ConstantBusinessException.USER_SIGNUP_MISSING_PARAMS_EXCEPTION);
        }
    }

}
