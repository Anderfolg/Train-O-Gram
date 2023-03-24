package org.anderfolg.trainogram.exceptions;

import org.springframework.security.core.AuthenticationException;

public class Status441JwtAuthenticationException extends AuthenticationException {
    public Status441JwtAuthenticationException( String msg, Throwable cause ) {
        super(msg, cause);
    }

    public Status441JwtAuthenticationException( String msg ) {
        super(msg);
    }
}
