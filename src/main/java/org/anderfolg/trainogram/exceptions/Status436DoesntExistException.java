package org.anderfolg.trainogram.exceptions;

public class Status436DoesntExistException extends ErrorCodeException{
    private static final int CODE = 436;

    public Status436DoesntExistException( String message ) {
        super(CODE, message,"436");
    }
}
