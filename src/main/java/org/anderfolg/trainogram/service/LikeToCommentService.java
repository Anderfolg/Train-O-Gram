package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.Status420AlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.springframework.data.domain.Page;


public interface LikeToCommentService {
    Page<Like> findAllLikesByComment( Long commentID, int page, int size)
            throws Status436DoesntExistException;

    Page<Like> findAllLikesByUser(JwtUser jwtUser, int page, int size)
            throws Status419UserException;

    void addLikeToComment( JwtUser jwtUser, Long commentID)
            throws Status436DoesntExistException,
            Status420AlreadyExistsException,
            Status419UserException;

    void deleteLikeFromComment(JwtUser jwtUser, Long commentID)
            throws Status436DoesntExistException,
            Status419UserException;
}
