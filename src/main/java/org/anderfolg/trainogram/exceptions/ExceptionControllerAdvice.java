package org.anderfolg.trainogram.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity handleAlreadyExistsException(ErrorCodeException ex){
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }
    public ResponseEntity handleException(Exception ex){
        log.error("Got exception: ",ex);
        return new ResponseEntity(ExceptionUtils.getStackTrace(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
