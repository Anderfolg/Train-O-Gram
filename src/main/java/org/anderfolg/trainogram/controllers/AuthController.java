package org.anderfolg.trainogram.controllers;

import lombok.AllArgsConstructor;
import org.anderfolg.trainogram.entities.DTO.AuthRequestDTO;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.security.jwt.JwtTokenProvider;
import org.anderfolg.trainogram.service.AuthService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<Object,Object>> login( @RequestBody AuthRequestDTO requestDTO){
        Map<Object,Object> response = authService.login(requestDTO.getUsername(), requestDTO.getPassword());
        return ResponseEntity.ok(response);
    }
}