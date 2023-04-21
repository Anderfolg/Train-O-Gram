package org.anderfolg.trainogram.exceptions;


public class Status435StorageException extends ErrorCodeException{
    private static final int CODE = 435;

    public Status435StorageException( String message) {
        super(CODE, message,"435");
    }
}
