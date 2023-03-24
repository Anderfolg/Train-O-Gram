package org.anderfolg.trainogram.exceptions;

public class Status437AuthenticationFailException extends ErrorCodeException{
    private static final int CODE = 437;

    public Status437AuthenticationFailException( String message ) {
        super(CODE, message,"437");
    }
}

