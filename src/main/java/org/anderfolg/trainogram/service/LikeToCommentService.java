package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.Status420AlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface LikeToCommentService {
    List<Like> findAllLikesByComment( Long commentID )
            throws Status436DoesntExistException;

    List<Like> findAllLikesByUser( JwtUser jwtUser)
            throws Status419UserException;

    void addLikeToComment( JwtUser jwtUser, Long commentID)
            throws Status436DoesntExistException,
            Status420AlreadyExistsException,
            Status419UserException;

    void deleteLikeFromComment(JwtUser jwtUser, Long commentID)
            throws Status436DoesntExistException,
            Status419UserException;
}
