package org.anderfolg.trainogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Status432InvalidFileNameException extends ErrorCodeException{
    private static final int CODE = 432;

    public Status432InvalidFileNameException( String message ) {
        super(CODE, message,"430");
    }
}
