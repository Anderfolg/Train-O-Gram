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
/*    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role){
*//*        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority("ROLE_"+role.toString())
                ).collect(Collectors.toList());*//*
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.getAuthority()));
    }*/
}
