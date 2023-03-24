package org.anderfolg.trainogram.exceptions;

public class Status440LikeAlreadyExistsException extends ErrorCodeException{
    private static final int CODE = 440;

    public Status440LikeAlreadyExistsException( String message ) {
        super(CODE, message,"440");
    }
}
