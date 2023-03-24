package org.anderfolg.trainogram.security.jwt;

import io.jsonwebtoken.*;
import org.anderfolg.trainogram.entities.Role;
import org.anderfolg.trainogram.exceptions.Status441JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long validityInMilliseconds;

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider( UserDetailsService userDetailsService ) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init(){secret = Base64.getEncoder().encodeToString(secret.getBytes());}


    public String createToken( String username, Role role ){

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public Authentication getAuthentication( String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken( HttpServletRequest req ){
        String bearerToken = req.getHeader("Authorization");
        if ( bearerToken != null && bearerToken.startsWith("Bearer_")){
            return bearerToken.substring(7);
        }
        return null;
    }
    /*public String resolve(String bearerToken){
        if ( bearerToken != null){
            return bearerToken.substring(7);
        }
        return null;
    }*/
    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            throw new Status441JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

/*    private List<String> getRoleName(List<Role> userRoles){
        List<String> result = new ArrayList<>();
        userRoles.forEach(role -> result.add(role.toString()));
        return result;
    }*/
}
