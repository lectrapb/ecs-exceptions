package com.app.authorization_reactive.shared.common.domain.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException  {

    private final String code;
    private final String optionalInfo;

    public AppException(String message,
                        String code,
                        String optionalInfo) {
        super(message);
        this.code = code;
        this.optionalInfo = optionalInfo;
    }
}
