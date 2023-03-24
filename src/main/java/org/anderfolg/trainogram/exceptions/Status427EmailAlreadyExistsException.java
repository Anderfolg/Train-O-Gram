package org.anderfolg.trainogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Status427EmailAlreadyExistsException extends ErrorCodeException {

    private static final int CODE = 427;

    public Status427EmailAlreadyExistsException( String message ) {
        super(CODE, message,"427");
    }
}
