package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.DTO.FollowDto;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface FollowService {
    List<FollowDto> findFollowersByUser( User user);
    List<FollowDto> findFollowingsByUser(User user);
    void addFollowing(JwtUser jwtUser, Long userId)
            throws Status419UserException;
    void deleteFollower(Long id, JwtUser jwtUser)
            throws Status419UserException;
    void deleteFollowing(Long id, JwtUser jwtUser)
            throws Status419UserException;
}
