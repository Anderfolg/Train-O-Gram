package org.anderfolg.trainogram.security.jwt;

import org.anderfolg.trainogram.entities.User;

import java.util.List;

public class JwtUserFactory {
    public JwtUserFactory(){
    }
    public static JwtUser create( User user){
        return new JwtUser(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                true,
                List.of(user.getRole()));
    }
}
