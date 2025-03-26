package com.app.authorization_reactive.authorization.signup.domain.value;

import com.app.authorization_reactive.shared.common.domain.exception.BusinessException;
import com.app.authorization_reactive.shared.common.domain.exception.ConstantBusinessException;

public record UserName(String value) {

    public UserName {
        if (value == null || value.isEmpty()) {
            throw new BusinessException(ConstantBusinessException.USER_SIGNUP_MISSING_PARAMS_EXCEPTION);
        }
    }
}
