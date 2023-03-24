package org.anderfolg.trainogram.exceptions;

public class Status433InvalidTokenException extends ErrorCodeException{
    private static final int CODE = 433;

    public Status433InvalidTokenException( String message ) {
        super(CODE, message,"433");
    }
}
