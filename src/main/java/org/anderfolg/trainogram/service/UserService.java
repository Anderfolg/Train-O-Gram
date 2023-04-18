package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.dto.UserDto;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status420AlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status419UserException;

import java.util.List;

public interface UserService {
    User findByUsername( String username);

    User createUser( UserDto userDto)
            throws Status419UserException,
            Status420AlreadyExistsException;

    void deleteUser(Long id);

    User updateUser(UserDto userDto, Long id)
            throws Status419UserException,
            Status420AlreadyExistsException;

    List<User> findAllUsers();

    User findUserById(Long id)
            throws Status419UserException;

    User findAuthenticatedUser();
    User findAndAuthenticateUserFromToken(String token);
}
