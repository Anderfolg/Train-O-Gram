package org.anderfolg.trainogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Status420UsernameAlreadyExistsException extends ErrorCodeException {

    private static final int CODE = 420;

    public Status420UsernameAlreadyExistsException( String message ) {
        super(CODE, message,"420");
    }
}
