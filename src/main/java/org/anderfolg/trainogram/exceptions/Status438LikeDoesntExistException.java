package org.anderfolg.trainogram.exceptions;

public class Status438LikeDoesntExistException extends ErrorCodeException{
    private static final int CODE = 438;

    public Status438LikeDoesntExistException( String message ) {
        super(CODE, message,"438");
    }
}
