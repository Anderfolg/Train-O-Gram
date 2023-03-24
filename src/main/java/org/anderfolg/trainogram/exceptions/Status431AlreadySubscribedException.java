package org.anderfolg.trainogram.exceptions;

public class Status431AlreadySubscribedException extends ErrorCodeException{
    private static final int CODE = 431;

    public Status431AlreadySubscribedException( String message ) {
        super(CODE, message,"431");
    }
}
