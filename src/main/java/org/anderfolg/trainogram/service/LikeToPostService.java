package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface LikeToPostService {
    List<Like> findAllLikesByPost( Long id)
            throws Status436DoesntExistException;

    List<Like> findAllLikesByUser( JwtUser jwtUser)
            throws Status419UserException;
    void addLikeToPost(JwtUser jwtUser, Long postID)
            throws
            Status436DoesntExistException,
            Status419UserException;

    void deleteLikeFromPost( JwtUser jwtUser, Long postID)
            throws
            Status436DoesntExistException,
            Status419UserException;

}
