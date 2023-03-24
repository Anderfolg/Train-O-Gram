package org.anderfolg.trainogram.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@AllArgsConstructor
public class JwtControllerAdvice {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    @ModelAttribute
    public JwtUser getJwtUser( WebRequest request ){
        String token = request.getHeader("Authorization");
        if ( token!=null ) return (JwtUser) jwtUserDetailsService.loadUserByUsername(jwtTokenProvider.getUsername(token));
        else return null;
    }
}
