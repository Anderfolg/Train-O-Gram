package org.anderfolg.trainogram.exceptions;

public class Status436PostDoesntExistException extends ErrorCodeException{
    private static final int CODE = 436;

    public Status436PostDoesntExistException( String message ) {
        super(CODE, message,"436");
    }
}
