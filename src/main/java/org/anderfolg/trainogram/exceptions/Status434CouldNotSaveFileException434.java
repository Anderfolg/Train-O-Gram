package org.anderfolg.trainogram.exceptions;

//  TODO (Bogdan O.) 7/4/23: needs to be renamed
public class Status434CouldNotSaveFileException434 extends ErrorCodeException{
    private static final int CODE = 434;

    public Status434CouldNotSaveFileException434( String message ) {
        super(CODE, message,"434");
    }
}
