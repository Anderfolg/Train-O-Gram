package org.anderfolg.trainogram.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.DTO.UserDto;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status427EmailAlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status420UsernameAlreadyExistsException;
import org.anderfolg.trainogram.repo.UserRepository;
import org.anderfolg.trainogram.security.jwt.JwtTokenProvider;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.anderfolg.trainogram.entities.Role.USER;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final Cache userCache;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider, CacheManager cacheManager ){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userCache = cacheManager.getCache("userCache");
    }

    @Override
    public User findByUsername( String username ) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public User createUser ( UserDto userDto ) throws Status420UsernameAlreadyExistsException, Status427EmailAlreadyExistsException {
        log.info("registering user {}", userDto.getUsername());

        if(userRepository.existsByUsername(userDto.getUsername())) {
            log.warn("username {} already exists.", userDto.getUsername());

            throw new Status420UsernameAlreadyExistsException(String.format("username %s already exists", userDto.getUsername()));
        }

        if(userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("email {} already exists.", userDto.getEmail());

            throw new Status427EmailAlreadyExistsException(
                    String.format("email %s already exists", userDto.getEmail()));
        }
            String encodedPassword = bCryptPasswordEncoder.encode(userDto.getPassword());
            User newUser = User.builder()
                    .username(userDto.getUsername())
                    .password("{bcrypt}"+encodedPassword)
                    .email(userDto.getEmail())
                    .role(USER)
                    .build();
            return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void deleteUser( Long id ) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) throws Status420UsernameAlreadyExistsException, Status427EmailAlreadyExistsException, Status419UserException {
        log.info("updating user {}", userDto.getUsername());

        if (userRepository.existsByUsername(userDto.getUsername()) || userRepository.existsByEmail(userDto.getEmail())) {
            String message = String.format("%s already exists", userRepository.existsByUsername(userDto.getUsername()) ? "username" : "email");
            log.warn("{} {}", message, userDto.getUsername());

            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new Status420UsernameAlreadyExistsException(message);
            } else {
                throw new Status427EmailAlreadyExistsException(message);
            }
        }

        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new Status419UserException("User does not exist"));

        updatedUser.setUsername(userDto.getUsername());
        updatedUser.setEmail(userDto.getEmail());
        String encodedPassword = "{bcrypt}" + bCryptPasswordEncoder.encode(userDto.getPassword());
            updatedUser.setPassword(encodedPassword);

        userRepository.save(updatedUser);
        return updatedUser;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "userCache", key = "#userId")
    @Override
    public User findUserById(Long userId) throws Status419UserException {
        User cachedUser = (User) userCache.get(userId);
        if (cachedUser != null) {
            return cachedUser;
        } else {
            Optional<User> optionalUser = userRepository.findById(userId);
            User dbUser = optionalUser.orElseThrow(() -> new Status419UserException("user not found"));
            userCache.put(userId, dbUser);
            return dbUser;
        }
    }

    @Override
    public User findAuthenticatedUser() {
        return userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new UsernameNotFoundException(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public User findAndAuthenticateUserFromToken(String token){
        jwtTokenProvider.getAuthentication(token);
        return findByUsername(jwtTokenProvider.getUsername(token));
    }//for jwt user caching
}