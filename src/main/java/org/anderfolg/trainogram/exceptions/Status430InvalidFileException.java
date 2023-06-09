package org.anderfolg.trainogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Status430InvalidFileException extends ErrorCodeException{
    private static final int CODE = 430;

    public Status430InvalidFileException( String message ) {
        super(CODE, message,"430");
    }
}
