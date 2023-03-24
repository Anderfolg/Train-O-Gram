package org.anderfolg.trainogram.exceptions;

import java.io.IOException;

public class Status435StorageException extends ErrorCodeException{
    private static final int CODE = 435;

    public Status435StorageException( String message, IOException e ) {
        super(CODE, message,"435");
    }
}
