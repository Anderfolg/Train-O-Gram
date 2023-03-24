package org.anderfolg.trainogram.exceptions;

public class Status439CommentDoesntExistException extends ErrorCodeException{
    private static final int CODE = 439;

    public Status439CommentDoesntExistException( String message ) {
        super(CODE, message,"439");
    }
}
