package org.anderfolg.trainogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Status419UserException extends ErrorCodeException{
    private static final int CODE = 419;

    public Status419UserException( String message ) {
        super(CODE, message,"419");
    }
}
