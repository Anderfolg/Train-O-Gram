package org.anderfolg.trainogram.controllers;

import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.dto.UserDto;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status420AlreadyExistsException;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        Page<User> users = userService.findAllUsers(pageNo, pageSize);
        return new ResponseEntity<>(users.getContent(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> saveUser( @RequestBody UserDto userDto ) throws Status420AlreadyExistsException, Status419UserException {
        User newUser =  userService.createUser(userDto);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser( @RequestBody UserDto userDto ,@PathVariable Long id) throws Status420AlreadyExistsException, Status419UserException {
        User newUser =  userService.updateUser(userDto, id);
        return ResponseEntity.ok(newUser);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> findById( @PathVariable Long id ) throws Status419UserException {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
