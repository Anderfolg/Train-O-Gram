package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.springframework.data.domain.Page;


public interface LikeToPostService {
    Page<Like> findAllLikesByPost( Long postID, int page, int size)
            throws Status436DoesntExistException;

    Page<Like> findAllLikesByUser( JwtUser jwtUser, int page, int size)
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
