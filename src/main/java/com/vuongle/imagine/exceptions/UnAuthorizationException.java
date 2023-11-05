package com.vuongle.imagine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnAuthorizationException extends RuntimeException {


    public UnAuthorizationException(String message) {
        super(message);
    }

    public UnAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

}
