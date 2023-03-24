package org.anderfolg.trainogram.controllers;

import io.jsonwebtoken.Jwt;
import org.anderfolg.trainogram.entities.DTO.UserDto;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status427EmailAlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status420UsernameAlreadyExistsException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> allUsers(){
        List<User> response = userService.findAllUsers();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> saveUser( @RequestBody UserDto userDto ) throws Status420UsernameAlreadyExistsException, Status419UserException, Status427EmailAlreadyExistsException {
        User newUser =  userService.createUser(userDto);
        return ResponseEntity.ok(newUser);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser( @RequestBody UserDto userDto ,@PathVariable Long id) throws Status420UsernameAlreadyExistsException, Status419UserException, Status427EmailAlreadyExistsException {
        User newUser =  userService.updateUser(userDto, id);
        return ResponseEntity.ok(newUser);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> findById( @PathVariable Long id ) throws Status419UserException {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/checkAdmin")
    public ResponseEntity<String> checkAdmin( JwtUser jwtUser ){
        String response = jwtUser.username();
        return ResponseEntity.ok(response);
    }
}
